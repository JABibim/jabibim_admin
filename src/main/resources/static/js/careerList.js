function updateCareerActive(careerName, displayStatus) {
    let contextPath = /*[[${contextPath}]]*/ '';

    $.ajax({
        url: contextPath + '/teacher/updateCareerActive',
        type: 'post',
        data: { careerName, displayStatus},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: (res) => {
            console.log('약력의 활성화 여부 업데이트가 정상적으로 처리되었습니다.');
            alert('약력 상태가 성공적으로 업데이트되었습니다.');
        },
        error: (err) => {
            console.error('약력의 활성화 여부를 업데이트 하는 도중 오류가 발생했습니다.', err);
            alert('약력 상태 업데이트 중 오류가 발생했습니다. 다시 시도해주세요.');
        }
    });
}

$(function () {
    // 이벤트 위임을 사용하여 .toggle-display-status에 이벤트 핸들러 바인딩
    $('table').on('change', '.toggle-display-status', function () {
        let careerName = $(this).data('career-name');
        let isChecked = $(this).is(':checked') ? 1 : 0;

        // 디버깅 출력
        console.log('선택된 Career:', careerName);
        console.log('선택 상태:', isChecked);

        if (careerName) {
            if (isChecked) {
                // 다른 체크박스 모두 해제
                $('.toggle-display-status').not(this).prop('checked', false);

                // 선택된 항목은 1로 설정하고 나머지는 0으로 업데이트
                updateCareerActive(careerName, 1);
                $('.toggle-display-status').not(this).each(function () {
                    updateCareerActive($(this).data('career-name'), 0);
                });
            } else {
                updateCareerActive(careerName, 0);
            }
        } else {
            console.error('career_name을 가져오지 못했습니다.');
            alert('약력 이름을 가져오는 중 문제가 발생했습니다.');
        }
    });
});
