const ACADEMY_NAME_MAX_LENGTH = 30;

function getAcademyList() {
    $('#spinner').show();

    $.ajax({
        data: {},
        url: 'manageAcademy/getAcademyList',
        dataType: 'json',
        cache: false,
        success: function (data) {
            $('#academySelect').remove();

            $('#spinner').hide();

            displayAcademyList(data.academyList);
        }, error: function () {
            $('#spinner').hide();

            showToast('학원 목록 Ajax 호출 도중 에러가 발생했습니다.');
            console.log('학원 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

function displayAcademyList(academyList) {
    let output = `
    <div class="d-flex justify-content-between" id="academySelect">
        <div class="flex-grow-1 me-2" id="academyList">
            <select class="form-select" aria-label="Default select example" id="academySelectForm">
    `;

    if (academyList <= 0) {
        output += `<option value="0" selected>학원이 존재하지 않습니다.</option>`;
    } else {
        output += `<option value="0" selected disabled>학원을 선택해주세요.</option>`;

        for (let i = 0; i < academyList.length; i++) {
            const {academyId, academyName} = academyList[i];
            output += `<option value="${academyId}">${academyName}</option>`
        }
    }

    output += `
            </select>
        </div>
        <div class="flex-shrink-0">
            <button data-bs-toggle="tooltip" data-bs-placement="top" title="학원 추가하기" class="btn btn-success" id="addAcademyBtn">+</button>
        </div>
    </div>
    `;

    $('#selectForm').append(output);
    $('[data-bs-toggle="tooltip"]').tooltip();
}

function callDaumAddressApi() {
    new daum.Postcode({
        oncomplete: function (data) {
            const {userSelectedType, zonecode, address, jibunAddress} = data;
            let addr = userSelectedType === 'R' ? address : jibunAddress; // R은 도로명, J는 지번

            $('#academy_postal_code').val(zonecode);
            $('#academy_address').val(addr);
        }
    }).open();
}

function validCheckAcademyRegisNum(businessRegisNum) {
    return new Promise((resolve, reject) => {
        $.ajax({
            data: {businessRegisNum},
            url: 'manageAcademy/checkBusinessRegisNum',
            dataType: 'json',
            cache: false,
            success: function (response) {
                if (response) {
                    resolve(true);
                } else {
                    resolve(false);
                }
            },
            error: function () {
                showToast('사업자 번호 유효성 검사 Ajax 호출 도중 에러가 발생했습니다.');
                console.log('사업자 번호 유효성 검사 Ajax 호출 도중 에러가 발생했습니다.');
                reject(new Error('Ajax 호출 실패')); // 실패 시 reject 호출
            }
        });
    });
}

function addNewAcademy(newAcademyName, newAcademyOwner, newAcademyContect, newRegisteredAt, newAcademyPostalcode, newAcademyAddress, newDetailAcademyAddress, newBusinessRegisNum) {
    $.ajax({
        data: JSON.stringify({
            academyName: newAcademyName,
            academyOwner: newAcademyOwner,
            academyContect: newAcademyContect,
            registeredAt: newRegisteredAt,
            academyAddress: newAcademyAddress,
            academyDetailAddr: newDetailAcademyAddress,
            academyPostalcode: newAcademyPostalcode,
            businessRegisNum: newBusinessRegisNum
        }),
        url: 'manageAcademy/add',
        dataType: 'json',
        contentType: 'application/json',
        type: 'POST',
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#card').remove();

            showToast('학원 정보가 정상적으로 추가되었습니다.');
            getAcademyList();

            displayCard(data);
        }, error: function () {
            showToast('학원 추가 Ajax 호출 도중 에러가 발생했습니다.');
            console.log('학원 추가 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

function getAcademyDetail(academyId) {
    $.ajax({
        data: {academyId},
        url: 'manageAcademy/detail',
        dataType: 'json',
        cache: false,
        success: function (data) {
            $('#card').remove();

            displayCard(data.academyDetailInfo);
        }, error: function () {
            console.log('학원 상세정보 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

function updateAcademyDetail(academyId, academyName, deletedAt, academyPostalCode, academyAddress, academyDetailAddress) {
    $.ajax({
        data: {academyId, academyName, deletedAt, academyPostalCode, academyAddress, academyDetailAddress},
        url: 'manageAcademy/modify',
        dataType: 'json',
        cache: false,
        success: function (data) {
            $('#card').remove();
            showToast('학원 정보가 정상적으로 수정되었습니다.');
            getAcademyList();

            displayCard(data.academyDetailInfo);
        }, error: function () {
            showToast('학원 상세정보 업데이트 Ajax 호출 도중 에러가 발생했습니다.');
            console.log('학원 상세정보 업데이트 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

function validCheck(value, name, createdAt) {
    if (name === '이름') {
        if (value.length > ACADEMY_NAME_MAX_LENGTH) {
            showToast('학원 이름은 영문,한글,띄어쓰기 포함 총 ' + ACADEMY_NAME_MAX_LENGTH + '자 이내여야 합니다.');
            return false;
        }
    }

    if (name === '탈퇴일') {
        const inputDate = new Date(value);
        const createdDate = new Date(createdAt);

        // 가입 날짜보다 작은 경우 false 반환
        if (inputDate < createdDate) {
            showToast('탈퇴일자는 가입일자보다 이후여야 합니다.');
            return false;
        }
    }

    if (value === null || value === '') {
        showToast(name + ' 값을 확인해주세요.');
        return false;
    }

    return true;
}

function showToast(message) {
    // 토스트 메시지 업데이트
    $('#validationToast .toast-body').text(message);

    // Bootstrap 토스트 객체 생성
    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    // 토스트 표시
    toast.show();
}

function displayCard(data) {
    const {
        academyId,
        academyName,
        academyOwner,
        academyAddress,
        academyDetailAddr,
        academyPostalcode,
        academyContect,
        businessRegisNum,
        registeredAt,
        deletedAt
    } = data.academy;

    let registeredAtParts = registeredAt && registeredAt.split(' ')[0].split('-');
    let formattedRegisteredDate = registeredAtParts[0] + '-' + registeredAtParts[1] + '-' + registeredAtParts[2];

    let deletedAtParts = deletedAt ? deletedAt.split(' ')[0].split('-') : null;
    let formattedDeletedDate = deletedAtParts ? deletedAtParts[0] + '-' + deletedAtParts[1] + '-' + deletedAtParts[2] : '';

    let regis_num_parts = businessRegisNum.split('-');
    let regis_num_part1 = regis_num_parts[0];
    let regis_num_part2 = regis_num_parts[1];
    let regis_num_part3 = regis_num_parts[2];

    let output = `
        <div class="card" id="card">
                    <div class="card-body pt-3">
                        <ul class="nav nav-tabs nav-tabs-bordered" id="nav-tabs">
                            <li class="nav-item">
                                <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#profile-overview">
                                    상세 정보
                                </button>
                            </li>
                            <li class="nav-item">
                                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#profile-edit">
                                    정보 수정
                                </button>
                            </li>
                        </ul>

                        <div class="tab-content pt-2">
                            <!-- 학원 상세정보 -->
                            <div class="tab-pane fade show active profile-overview" id="profile-overview">
                                <h5 class="card-title">학원 상세정보</h5>
                                
                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label ">ID</div>
                                    <div class="col-lg-9 col-md-8">${academyId}</div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">학원 이름</div>
                                    <div class="col-lg-9 col-md-8">${academyName}</div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">대표자 명</div>
                                    <div class="col-lg-9 col-md-8">${academyOwner}</div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">대표 연락처</div>
                                    <div class="col-lg-9 col-md-8">${academyContect}</div>
                                </div>

                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">서비스 가입일</div>
                                    <div class="col-lg-9 col-md-8">${formattedRegisteredDate}</div>
                                </div>

                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">탈퇴일</div>
                                    <div class="col-lg-9 col-md-8">${formattedDeletedDate}</div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">우편번호</div>
                                    <div class="col-lg-9 col-md-8">${academyPostalcode}</div>
                                </div>

                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">주소</div>
                                    <div class="col-lg-9 col-md-8">${academyAddress}</div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">상세주소</div>
                                    <div class="col-lg-9 col-md-8">${academyDetailAddr}</div>
                                </div>

                                <div class="row">
                                    <div class="col-lg-3 col-md-4 label">사업자 번호</div>
                                    <div class="col-lg-9 col-md-8">${businessRegisNum}</div>
                                </div>
                            </div>

                            <!-- 학원 정보수정 -->
                            <div class="tab-pane fade profile-edit" id="profile-edit">
                                <h5 class="card-title">정보 수정</h5>
                                <form>
                                    <div class="row mb-3">
                                        <label for="about" class="col-md-4 col-lg-3 col-form-label">ID</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="modify_academy_id" type="text" class="form-control" id="modify_academy_id" th:value="${academyId}" disabled>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="company" class="col-md-4 col-lg-3 col-form-label">이름</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="modify_academy_name" type="text" class="form-control" id="modify_academy_name" th:value="${academyName}" maxlength="50">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Job" class="col-md-4 col-lg-3 col-form-label">가입일</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="date" class="form-control" id="modify_created_at" name="modify_created_at" th:value="${formattedRegisteredDate}" disabled>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Country" class="col-md-4 col-lg-3 col-form-label">탈퇴일</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="date" class="form-control" id="modify_deleted_at" name="modify_deleted_at" th:value="${formattedDeletedDate}">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Address" class="col-md-4 col-lg-3 col-form-label">주소</label>
                                        <div class="col-md-8 col-lg-9 d-flex flex-column flex-sm-row justify-content-between">
                                            <div class="me-1 col-md-2 mb-2 mb-sm-0">
                                                <input name="modify_academy_postal_code" type="text" class="form-control" id="academy_postal_code" placeholder="우편번호" th:value="${academyPostalcode}" readonly>
                                            </div>
                                            <div class="me-1 col-md-8 mb-2 mb-sm-0">
                                                <input name="modify_academy_address" type="text" class="form-control" id="academy_address" placeholder="주소를 입력해주세요." th:value="${academyAddress}" readonly >
                                            </div>
                                            <button class="btn btn-success d-flex align-items-center justify-content-center" id="callApiBtn" type="button">
                                                <span class="d-none d-sm-inline">주소검색</span>
                                                <i class="bi bi-search d-inline d-sm-none"></i>
                                            </button>
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="Address" class="col-md-4 col-lg-3 col-form-label">상세주소</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="modify_academy_detail_address" type="text" class="form-control" id="modify_academy_detail_address" placeholder="상세주소를 입력해주세요." th:value="${academyDetailAddr}">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Phone" class="col-md-4 col-lg-3 col-form-label">사업자번호</label>
                                        <div class="col-md-8 col-lg-9 d-flex">
                                                <input name="modify_academy_regis_num1" type="text" class="form-control me-1" id="modify_academy_regis_num1" th:value="${regis_num_part1}" oninput="this.value = this.value.replace(/[^0-9]/g, '');" disabled>
                                                <input name="modify_academy_regis_num2" type="text" class="form-control mx-1" id="modify_academy_regis_num2" th:value="${regis_num_part2}" oninput="this.value = this.value.replace(/[^0-9]/g, '');" disabled>
                                                <input name="modify_academy_regis_num3" type="text" class="form-control ms-1" id="modify_academy_regis_num3" th:value="${regis_num_part3}" oninput="this.value = this.value.replace(/[^0-9]/g, '');" disabled>
                                        </div>
                                    </div>
                                    <div style="text-align: right">
                                        <button type="button" class="btn btn-primary" id="academyModifyBtn">변경사항 저장</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
    `

    $('#detail-tabs').append(output);
}

function displayAddAcademyForm() {
    $('#card').remove();

    let output = `
        <div class="card" id="card">
                    <div class="card-body pt-3">
                        <div class="tab-content">
                            <!-- 학원 상세정보 -->
                            <div class="tab-pane fade show active profile-overview">
                                <h5 class="card-title">신규 학원정보</h5>
                                <!-- 학원 정보수정 -->
                                <form id="newProfile">
                                    <div class="row mb-3">
                                        <label for="new_academy_name" class="col-md-4 col-lg-3 col-form-label">학원 이름</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="academyName" type="text" class="form-control" id="new_academy_name" placeholder="학원 이름을 입력해주세요." maxlength="50">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="new_academy_owner" class="col-md-4 col-lg-3 col-form-label">대표자 명</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="academyOwner" type="text" class="form-control" id="new_academy_owner" placeholder="학원 대표자 이름을 입력해주세요." maxlength="50">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="company" class="col-md-4 col-lg-3 col-form-label">대표 연락처</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="academyContect" type="text" class="form-control" id="new_academy_contect" placeholder="학원 대표 연락처를 입력해주세요." maxlength="50">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Job" class="col-md-4 col-lg-3 col-form-label">서비스 가입일</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="date" class="form-control" id="new_registered_at" name="registeredAt">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Address" class="col-md-4 col-lg-3 col-form-label">주소</label>
                                        <div class="col-md-8 col-lg-9 d-flex flex-column flex-sm-row justify-content-between">
                                            <div class="me-1 col-md-2 mb-2 mb-sm-0">
                                                <input name="academyPostalcode" type="text" class="form-control" id="academy_postal_code" placeholder="우편번호" readonly>
                                            </div>
                                            <div class="me-1 col-md-8 mb-2 mb-sm-0">
                                                <input name="academyPostalcode" type="text" class="form-control" id="academy_address" placeholder="주소" readonly>
                                            </div>
                                            <button class="btn btn-success d-flex align-items-center justify-content-center" id="callApiBtn" type="button">
                                                <span class="d-none d-sm-inline">주소검색</span>
                                                <i class="bi bi-search d-inline d-sm-none"></i>
                                            </button>
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="Address" class="col-md-4 col-lg-3 col-form-label">상세주소</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="academyDetailAddr" type="text" class="form-control" id="new_academy_detail_address" placeholder="상세주소를 입력해주세요.">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Phone" class="col-md-4 col-lg-3 col-form-label">사업자번호</label>
                                        <div class="col-md-8 col-lg-9 d-flex">
                                                <input name="new_academy_regis_num1" type="text" class="form-control me-1" id="new_academy_regis_num1" value="" oninput="this.value = this.value.replace(/[^0-9]/g, '');" placeholder="사업자 앞자리">
                                                <input name="new_academy_regis_num2" type="text" class="form-control mx-1" id="new_academy_regis_num2" value="" oninput="this.value = this.value.replace(/[^0-9]/g, '');" placeholder="사업자 가운데자리">
                                                <input name="new_academy_regis_num3" type="text" class="form-control ms-1" id="new_academy_regis_num3" value="" oninput="this.value = this.value.replace(/[^0-9]/g, '');" placeholder="사업자 끝자리">
                                        </div>
                                    </div>
                                    <div style="text-align: right">
                                        <button type="button" class="btn btn-primary" id="academyAddSubmitBtn">등록하기</button>
                                    </div>
                                </form>
                        </div>
                    </div>
                </div>
    `

    $('#detail-tabs').append(output);
}

$(document).ready(function () {
    getAcademyList();

    $(document).on('change', '#academySelectForm', function () {
        let academyId = $(this).val();
        getAcademyDetail(academyId);
    })

    $(document).on('click', '#addAcademyBtn', function () {
        $('#academySelectForm').val('0');
        displayAddAcademyForm();
    })

    $(document).on('click', '#academyAddSubmitBtn', function () {
        let newAcademyName = $('#new_academy_name').val();
        let newAcademyOwner = $('#new_academy_owner').val();
        let newAcademyContect = $('#new_academy_contect').val();
        let newRegisteredAt = $('#new_registered_at').val();
        let newAcademyPostalcode = $('#academy_postal_code').val();
        let newAcademyAddress = $('#academy_address').val();
        let newDetailAcademyAddress = $('#new_academy_detail_address').val();
        let newAcademyRegisNum1 = $('#new_academy_regis_num1').val();
        let newAcademyRegisNum2 = $('#new_academy_regis_num2').val();
        let newAcademyRegisNum3 = $('#new_academy_regis_num3').val();

        if (!validCheck(newAcademyName, '학원 이름')) {
            return;
        }
        if (!validCheck(newAcademyOwner, '대표자 명')) {
            return;
        }
        if (!validCheck(newAcademyContect, '대표 연락처')) {
            return;
        }
        if (!validCheck(newRegisteredAt, '서비스 가입일')) {
            return;
        }
        if (!validCheck(newAcademyPostalcode, '우편번호')) {
            return;
        }
        if (!validCheck(newAcademyAddress, '주소')) {
            return;
        }
        if (!validCheck(newAcademyRegisNum1, '사업자번호 앞자리')) {
            return;
        }
        if (!validCheck(newAcademyRegisNum2, '사업자번호 가운데자리')) {
            return;
        }
        if (!validCheck(newAcademyRegisNum3, '사업자번호 마지막자리')) {
            return;
        }

        let newBusinessRegisNum = newAcademyRegisNum1 + '-' + newAcademyRegisNum2 + '-' + newAcademyRegisNum3;
        validCheckAcademyRegisNum(newBusinessRegisNum)
            .then(isValid => {
                if (isValid) {
                    addNewAcademy(newAcademyName, newAcademyOwner, newAcademyContect, newRegisteredAt, newAcademyPostalcode, newAcademyAddress, newDetailAcademyAddress, newBusinessRegisNum);
                } else {
                    showToast('이미 등록되어 있는 사업자 번호입니다.');
                }
            })
            .catch(error => {
                console.log('새로운 학원 등록중 에러 발생', error);
            })
    });

    $(document).on('click', '#academyModifyBtn', function () {
        let academyId = $('#modify_academy_id').val();
        let academyName = $('#modify_academy_name').val();
        let createdAt = $('#modify_created_at').val();
        let deletedAt = $('#modify_deleted_at').val();
        let academyPostalCode = $('#academy_postal_code').val();
        let academyAddress = $('#academy_address').val();
        let academyDetailAddress = $('#modify_academy_detail_address').val();

        if (!validCheck(academyName, '이름')) {
            return;
        }

        // 날짜 valid check의 경우 세개의 파라미터를 준 이유는 탈퇴일자를 입력할때 가입일자보다 과거이면 안되기 때문에 valid check함수에 같이 전달합니다.
        if (deletedAt && !validCheck(deletedAt, '탈퇴일', createdAt)) {
            return;
        }

        if (!validCheck(academyAddress, '주소')) {
            return;
        }

        updateAcademyDetail(academyId, academyName, deletedAt, academyPostalCode, academyAddress, academyDetailAddress);
    })

    $(document).on('click', '#callApiBtn', function (event) {
        event.preventDefault();
        callDaumAddressApi();
    })
})