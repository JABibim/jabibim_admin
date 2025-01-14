function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

function updateDeleteButtonState() {
    const rowCount = $('table tbody tr').length;

    if (rowCount === 1) {
        $('.deleteBtn').prop('disabled', true);
    } else {
        $('.deleteBtn').prop('disabled', false);
    }
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

function modifyGradeInfo(gradeId, gradeName, discountRate) {
    if (gradeName === '') {
        showToast('등급명을 확인해주세요.');
        return false;
    }
    if (discountRate === '') {
        showToast('등급할인율을 확인해주세요.');
        return false;
    }
    if (discountRate > 100) {
        showToast('등급할인율은 100을 넘을 수 없습니다.');
        return false;
    }

    $.ajax({
        url: `modifyGradeInfo`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            gradeId,
            gradeName,
            discountRate,
        }),
        success: function (res) {
            if (res.status === 'success') {
                localStorage.setItem('toastMessage', '등급 수정이 성공하였습니다.');

                location.reload();
            } else if (res.status === 'fail') {
                localStorage.setItem('toastMessage', '등급 수정이 실패하였습니다.');

                location.reload();
            }
        },
        error: function () {
            alert('수정에 실패했습니다.');
        },
    });
}

function addGrade(gradeName, discountRate) {
    $.ajax({
        url: '/grade/addGrade',
        type: 'POST',
        contentType: 'application/json',
        beforeSend : function(xhr)
        {   //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
            xhr.setRequestHeader(header, token);
        },
        data: JSON.stringify({
            gradeName,
            discountRate,
        }),
        success: function (res) {
            if (res.status === 'success') {
                localStorage.setItem('toastMessage', res.message);

                location.reload();
            } else if (res.status === 'fail') {
                localStorage.setItem('toastMessage', res.message);

                location.reload();
            }
        },
        error: function () {
            showToast('등급 추가에 실패했습니다.');
        },
    });
}

function deleteGrade(academyId, gradeId, newGradeId) {
    $.ajax({
        url: 'deleteGrade',
        type: 'post',
        dataType: 'json',
        cache: false,
        data: {
            academyId,
            gradeId,
            newGradeId,
        },
        success: (res) => {
            console.log('=========> res : ', res);
            const {deleteResult, effectedRow} = res;
            if (deleteResult) {
                const message = `등급 삭제가 성공하였습니다. (학생 ${effectedRow}명의 등급이 조정되었습니다.)`;
                localStorage.setItem('toastMessage', message);

                showToast(message);
                setTimeout(() => location.reload(), 1500); // 1.5초 뒤에 reload
            } else {
                const message = '등급 삭제가 실패하였습니다.';
                localStorage.setItem('toastMessage', message);

                showToast(message);
                setTimeout(() => location.reload(), 1500); // 1.5초 뒤에 reload
            }
        },
        error: (err) => {
            showToast('등급 삭제에 실패했습니다.');
        },
    })
}

$(function () {
    updateDeleteButtonState();

    // 수정 버튼 이벤트
    $(document).on('click', '.modifyBtn', function () {
        const $row = $(this).closest('tr'); // 클릭된 버튼의 부모 tr
        const gradeId = $(this).data('grade-id'); // grade ID
        const academyId = $(this).data('academy-id'); // academy ID


        // 기존 데이터를 data-* 속성에 저장
        const gradeName = $row.find('td:nth-child(2)').text().trim();
        const discountRate = $row.find('td:nth-child(3)').text().trim();

        $row.data('originalData', {
            gradeName,
            discountRate,
            academyId,
        });

        // td를 input 필드로 변환
        $row.find('td:nth-child(2)').html(`<input type="text" class="form-control" value="${gradeName}" />`);
        $row.find('td:nth-child(3)').html(`<input type="number" class="form-control" value="${discountRate}" />`);

        // 수정 버튼을 저장/취소 버튼으로 교체
        $row.find('td:nth-child(5)').html(`
            <button class="btn btn-outline-primary saveBtn" data-grade-id="${gradeId}">
                <i class="bi bi-save"></i> 저장
            </button>
            <button class="btn btn-outline-secondary cancelBtn" data-grade-id="${gradeId}">
                <i class="bi bi-x-circle"></i> 취소
            </button>
        `);
    });

    // 취소 버튼 이벤트
    $(document).on('click', '.cancelBtn', function () {
        const $row = $(this).closest('tr'); // 클릭된 버튼의 부모 tr
        const originalData = $row.data('originalData'); // 저장된 원본 데이터 가져오기

        if (originalData) {
            // td를 원래 데이터로 복원
            $row.find('td:nth-child(2)').text(originalData.gradeName);
            $row.find('td:nth-child(3)').text(originalData.discountRate);

            // 저장/취소 버튼을 수정/삭제 버튼으로 복원
            const gradeId = $(this).data('grade-id');
            const academyId = originalData.academyId;

            $row.find('td:nth-child(5)').html(`
                <button class="btn btn-outline-success modifyBtn" data-grade-id="${gradeId}" data-academy-id="${academyId}"><i class="bi bi-pencil"></i> 수정</button>
                <button class="btn btn-outline-danger deleteBtn" data-grade-id="${gradeId}" data-academy-id="${academyId}" data-bs-toggle="modal" data-bs-target="#verticalycentered" ${gradeListSize === 1 ? 'disabled' : ''}><i class="bi bi-trash"></i> 삭제</button>
            `);

            updateDeleteButtonState();
        } else {
            console.warn('원본 데이터가 없습니다.');
        }
    });

    // 저장 버튼 이벤트
    $(document).on('click', '.saveBtn', function () {
        const $row = $(this).closest('tr');
        const gradeId = $(this).data('grade-id');
        const updatedGradeName = $row.find('td:nth-child(2) input').val();
        const updatedDiscountRate = $row.find('td:nth-child(3) input').val();

        // 서버에 데이터 전송
        modifyGradeInfo(gradeId, updatedGradeName, updatedDiscountRate);
    });

    $(document).on('click', '.addBtn', function () {
        const $tableBody = $('table tbody');

        // "등급 추가하기" 버튼 숨기기
        $(this).hide();

        // 마지막 행의 순위를 가져오기
        let lastRank = 0;
        const $lastRow = $tableBody.find('tr:last-child');
        if ($lastRow.length > 0 && !$lastRow.hasClass('new-grade-row')) {
            lastRank = parseInt($lastRow.find('td:first-child').text().trim()) || 0;
        }

        // 새로운 행 추가
        const newRow = `
            <tr class="new-grade-row">
                <td>${lastRank + 1}</td>
                <td>
                    <input type="text" class="form-control grade-name-input" placeholder="등급명을 입력하세요" />
                </td>
                <td>
                    <input type="number" class="form-control grade-discount-input" placeholder="할인율을 입력하세요" />
                </td>
                <td>0</td>
                <td style="text-align: end">
                    <button class="btn btn-outline-primary saveNewGradeBtn">
                        <i class="bi bi-plus-circle"></i> 추가
                    </button>
                    <button class="btn btn-outline-secondary cancelNewGradeBtn">
                        <i class="bi bi-x-circle"></i> 취소
                    </button>
                </td>
            </tr>
        `;
        $tableBody.append(newRow);
    })

    // 추가 버튼 클릭 이벤트
    $(document).on('click', '.saveNewGradeBtn', function () {
        const $row = $(this).closest('tr');
        const gradeName = $row.find('.grade-name-input').val();
        const discountRate = $row.find('.grade-discount-input').val();

        // 유효성 검사
        if (!gradeName || discountRate === '') {
            showToast('모든 값을 입력하세요.');
            return;
        }

        if (discountRate > 100) {
            showToast('등급할인율을 확인해주세요.(<=100)');
            return;
        }

        addGrade(gradeName, discountRate);
    });

    // 취소 버튼 클릭 이벤트
    $(document).on('click', '.cancelNewGradeBtn', function () {
        // 새로 추가된 행 삭제
        $(this).closest('tr').remove();

        // "등급 추가하기" 버튼 다시 표시
        $('.addBtn').show();
    });

    $(document).on('click', '.deleteBtn', function () {
        const gradeId = $(this).data('grade-id');
        const academyId = $(this).data('academy-id');

        $('.confirmDeleteBtn').data('grade-id', gradeId);
        $('.confirmDeleteBtn').data('academy-id', academyId);

        getUpdatableGradeList(academyId, gradeId);
    })

    $(document).on('click', '.confirmDeleteBtn', function () {
        const gradeId = $(this).data('grade-id');
        const academyId = $(this).data('academy-id');
        const newGradeId = $('#updatableGradeSelect').val();

        if (!newGradeId) {
            showToast('업데이트할 등급을 선택하세요.');
            return;
        }

        deleteGrade(academyId, gradeId, newGradeId);
    });

    // [chan] TOAST 메세지 출력용입니다. 화면에서 reload 가 일어나기 때문에 추가한 로직입니다.
    const toastMessage = localStorage.getItem('toastMessage');
    if (toastMessage) {
        showToast(toastMessage);
        localStorage.removeItem('toastMessage');
    }
});
