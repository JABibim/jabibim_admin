function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

function getUpdatableGradeList(academyId, gradeId) {
    $.ajax({
        data: {
            academyId, gradeId
        }
        , url: 'getUpdatableGradeList'
        , dataType: 'json'
        , cache: false
        , success: function (data) {
            console.log('=======> data : ', data);

            // 셀렉트 박스 업데이트
            const $select = $('#updatableGradeSelect');
            $select.empty(); // 기존 옵션 삭제
            $select.append('<option value="" selected disabled>선택하세요</option>'); // 기본 옵션 추가

            if (data.gradeList && data.gradeList.length > 0) {
                data.gradeList.forEach(grade => {
                    $select.append(`<option value="${grade.grade_id}">${grade.grade_name}</option>`);
                });
            } else {
                $select.append('<option value="">업데이트 가능한 등급이 없습니다.</option>');
            }
        }, error: function () {
            console.log('업데이트 가능한 등급 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

function updateStudentGrade(studentId, gradeId) {
    $.ajax({
        data: {
            studentId, gradeId
        }
        , url: 'updateStudentGrade'
        , dataType: 'json'
        , cache: false
        , success: function (res) {
            if (res.status === 'success') {
                showToast("등급 조정이 성공하였습니다.");
                setTimeout(() => location.reload(), 1500);
            } else {
                showToast("등급 조정이 실패하였습니다.");
                setTimeout(() => location.reload(), 1500);
            }
        }, error: function () {
            console.log('업데이트 가능한 등급 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

$(function () {
    $(document).on('click', '#modifyBtn', function () {
        let studentId = $('#modifyBtn').data('student-id');
        let academyId = $('#modifyBtn').data('academy-id');
        let gradeId = $('#modifyBtn').data('grade-id');

        const modal = new bootstrap.Modal('#verticalycentered', {
            keyboard: false
        });

        // 모달창 누르고 그 후 $(this)로 가져오기 위한 값 셋팅합니다.
        $('.confirmUpdateBtn').data('grade-id', gradeId);
        $('.confirmUpdateBtn').data('student-id', studentId);

        modal.show();
        getUpdatableGradeList(academyId, gradeId);
    })

    $(document).on('click', '.confirmUpdateBtn', function () {
        let gradeId = $('#updatableGradeSelect').val();
        let studentId = $(this).data('student-id');

        updateStudentGrade(studentId, gradeId);
    })
})