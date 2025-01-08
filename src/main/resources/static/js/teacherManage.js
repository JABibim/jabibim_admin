function addNewTeacher(academyId, teacherName, teacherPhone, teacherEmail, teacherPassword, authRole) {
    $('#spinner').show();

    $.ajax({
        data: JSON.stringify({academyId, teacherName, teacherPhone, teacherEmail, teacherPassword, authRole}),
        url: 'manageTeacher/add',
        dataType: 'json',
        contentType: 'application/json',
        type: 'POST',
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
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
            const {academyId, academyName} = academyList[i];
            output += `<option value="${academyId}">${academyName}</option>`
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
        success: function (res) {
            $('#card').remove();
            displayCard(academyId, res.data.teacherList);
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
        const {teacherId, teacherName, teacherEmail, teacherPassword} = data[i];
        output += `
            <li data-bs-toggle="tooltip" data-bs-placement="top" title="pwd : ${teacherPassword}">
                ${teacherId} ---- ${teacherEmail} ---- ${teacherName}
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
                                    <input type="hidden" value="${academyId}" id="academyId" name="academyId">
                                
                                    <div class="row mb-3">
                                        <label for="teacherName" class="col-md-4 col-lg-3 col-form-label">이름</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="text" class="form-control" id="teacherName" name="teacherName">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="teacherPhone" class="col-md-4 col-lg-3 col-form-label">전화번호</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="text" class="form-control" id="teacherPhone" name="teacherPhone">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="teacherEmail" class="col-md-4 col-lg-3 col-form-label">이메일</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="email" class="form-control" id="teacherEmail" name="teacherEmail">
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <label for="teacherPassword" class="col-md-4 col-lg-3 col-form-label">임시 비밀번호</label>
                                        <div class="col-md-8 col-lg-9">
                                            <input type="password" class="form-control" id="teacherPassword" name="teacherPassword">
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="authRole" class="col-md-4 col-lg-3 col-form-label">시스템 권한</label>
                                        <div class="d-flex justify-content-between" id="authRoleSelect">
                                            <div class="flex-grow-1 me-2" id="authRoleList">
                                                <select class="form-select" aria-label="Default select example" id="authRole" name="authRole">
                                                    <option value="0" selected disabled>권한을 선택해주세요.</option>
                                                    <option value="ROLE_MANAGER">ROLE_MANAGER</option>
                                                    <option value="ROLE_LECTURER">ROLE_LECTURER</option>
                                                </select>
                                            </div>
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
        let teacherName = $('#teacherName').val();
        let teacherPhone = $('#teacherPhone').val();
        let teacherEmail = $('#teacherEmail').val();
        let teacherPassword = $('#teacherPassword').val();
        let authRole = $('#authRole').val();

        if (!validCheck(teacherName, '강사 이름')) {
            return;
        }
        if (!validCheck(teacherPhone, '휴대폰 번호')) {
            return;
        }
        if (!validCheck(teacherEmail, '이메일')) {
            return;
        }
        if (!validCheck(teacherPassword, '비밀번호')) {
            return;
        }
        if (!validCheck(authRole, '강사 권한')) {
            return;
        }

        addNewTeacher(academyId, teacherName, teacherPhone, teacherEmail, teacherPassword, authRole);
    })

    $(document).on('click', '#callApiBtn', function (event) {
        event.preventDefault();
        callDaumAddressApi();
    })
})

