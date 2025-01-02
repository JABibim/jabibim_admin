function addNewTeacher(academyId, teacherName, postalCode, address, detailAddress, phone, email, password) {
    $('#spinner').show();
    $.ajax({
        data: {academyId, teacherName, postalCode, address, detailAddress, phone, email, password},
        url: 'manageTeacher/addNewTeacher',
        dataType: 'json',
        cache: false,
        success: function (data) {
            $('#card').remove();

            $('#spinner').hide();

            getTeacherList(academyId);
        }, error: function () {
            $('#spinner').hide();

            showToast('학원 강사 추가 Ajax 호출 도중 에러가 발생했습니다.');
            console.log('학원 강사 추가 Ajax 호출 도중 에러가 발생했습니다.');
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
            const {academy_id, academy_name} = academyList[i];
            output += `<option value="${academy_id}">${academy_name}</option>`
        }
    }

    output += `
            </select>
        </div>
    </div>
    `;

    $('#selectForm').append(output);
}

function callDaumAddressApi() {
    new daum.Postcode({
        oncomplete: function (data) {
            const {userSelectedType, zonecode, address, jibunAddress} = data;
            let addr = userSelectedType === 'R' ? address : jibunAddress; // R은 도로명, J는 지번

            $('#postal_code').val(zonecode);
            $('#address').val(addr);
        }
    }).open();
}

function getTeacherList(academyId) {
    $.ajax({
        data: {academyId},
        url: 'manageTeacher/getTeacherList',
        dataType: 'json',
        cache: false,
        success: function (data) {
            $('#card').remove();

            let teacherList = data.teacherList;

            displayCard(academyId, teacherList);
        }, error: function () {
            $('#spinner').hide();

            showToast('학원 목록 Ajax 호출 도중 에러가 발생했습니다.');
            console.log('학원 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

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

function validCheck(value, name, createdAt) {
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

function displayCard(academyId, data) {
    let output = `
        <div class="card" id="card">
                    <div class="card-body pt-3">
                        <ul class="nav nav-tabs nav-tabs-bordered" id="nav-tabs">
                            <li class="nav-item">
                                <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#profile-overview">
                                    강사 목록
                                </button>
                            </li>
                            <li class="nav-item">
                                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#profile-edit">
                                    강사 추가
                                </button>
                            </li>
                        </ul>

                        <div class="tab-content pt-2">
                            <!-- 학원 상세정보 -->
                            <div class="tab-pane fade show active profile-overview" id="profile-overview">
                                <div class="info-box mt-2">
                                    <p>
                                        <i class="bi bi-question-circle-fill"></i>&nbsp;&nbsp;노출 순서 : TEACHER_ID ---- TEACHER_EMAIL ---- TEACHER_NAME
                                    </p>
                                    <p>
                                        <i class="bi bi-question-circle-fill"></i>&nbsp;&nbsp;학원 데이터를 먼저 생성한 뒤 본 작업을 진행해주세요.<br>
                                        &nbsp;&nbsp;강사 추가를 진행하면 먼저 해당 강사에게 부여 가능한 직급 테이블(JOB)의 데이터를 찾습니다.<br>
                                        &nbsp;&nbsp;<u>사용 가능한 데이터가 있는 경우</u> 기본 직급으로 "강사(JOB_UNIT = 3)"을 부여하며 강사 데이터를 생성하고,<br>
                                        &nbsp;&nbsp;<u>그렇지 않은 경우</u> 해당 ACADEMY_ID에 해당하는 JOB TABLE의 데이터를 만들고 만들어진 직급 정보를 강사에게 부여합니다.    
                                    </p>
                                </div>
                                <h5 class="card-title">강사 목록</h5>
                                <div id="teachers">
                                    <div id="teacherList">
    `;
    output += `<ul>`;

    for (let i = 0; i < data.length; i++) {
        const {teacher_id, teacher_name, teacher_email, teacher_password, job_id} = data[i];
        output += `
            <li data-bs-toggle="tooltip" data-bs-placement="top" title="pwd : ${teacher_password}">
                ${teacher_id} ---- ${teacher_email} ---- ${teacher_name}
            </li>
        `;
    }

    output += `</ul>`;

    output += `                     </div>
                                </div>
                            </div>

                            <!-- 신규 강사 정보 입력 Form -->
                            <div class="tab-pane fade profile-edit" id="profile-edit">
                                <h5 class="card-title">강사 정보</h5>
                                <form>
                                    <input type="hidden" value="${academyId}" id="academyId">
                                
                                    <div class="row mb-3">
                                        <label for="about" class="col-md-4 col-lg-3 col-form-label">이름</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input name="teacher_name" type="text" class="form-control" id="teacher_name">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="Country" class="col-md-4 col-lg-3 col-form-label">이메일</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="email" class="form-control" id="email" name="email">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="Country" class="col-md-4 col-lg-3 col-form-label">임시 비밀번호</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="password" class="form-control" id="password" name="password">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="Address" class="col-md-4 col-lg-3 col-form-label">주소</label>
                                        <div class="col-md-8 col-lg-9 d-flex flex-column flex-sm-row justify-content-between">
                                            <div class="me-1 col-md-2 mb-2 mb-sm-0">
                                                <input name="postal_code" type="text" class="form-control" id="postal_code" placeholder="우편번호" value="" readonly>
                                            </div>
                                            <div class="me-1 col-md-8 mb-2 mb-sm-0">
                                                <input name="address" type="text" class="form-control" id="address" placeholder="주소를 입력해주세요." value="" readonly >
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
                                            <input name="detail_address" type="text" class="form-control" id="detail_address" placeholder="상세주소를 입력해주세요.">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="Job" class="col-md-4 col-lg-3 col-form-label">전화번호</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="text" class="form-control" id="phone" name="phone">
                                        </div>
                                    </div>
                                    
                                    <div style="text-align: right">
                                        <button type="button" class="btn btn-primary" id="teacherAddBtn">신규 강사 등록</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
    `

    $('#detail-tabs').append(output);
    $('[data-bs-toggle="tooltip"]').tooltip();
}

$(document).ready(function () {
    getAcademyList();

    $(document).on('change', '#academySelectForm', function () {
        let academyId = $(this).val();
        getTeacherList(academyId);
    })

    $(document).on('click', '#teacherAddBtn', function () {
        let academyId = $('#academyId').val();
        let teacherName = $('#teacher_name').val();
        let postalCode = $('#postal_code').val();
        let address = $('#address').val();
        let detailAddress = $('#detail_address').val();
        let phone = $('#phone').val();
        let email = $('#email').val();
        let password = $('#password').val();

        if (!validCheck(teacherName, '강사이름')) {
            return;
        }
        if (!validCheck(postalCode, '우편번호')) {
            return;
        }
        if (!validCheck(address, '주소')) {
            return;
        }
        if (!validCheck(phone, '휴대폰번호')) {
            return;
        }
        if (!validCheck(email, '이메일')) {
            return;
        }

        addNewTeacher(academyId, teacherName, postalCode, address, detailAddress, phone, email, password);
    })

    $(document).on('click', '#callApiBtn', function (event) {
        event.preventDefault();
        callDaumAddressApi();
    })
})

