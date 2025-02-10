
function go(page) {

    const limit = $('#viewcount').val();
    const data = {limit: limit, state: 'ajax', page: page};
    ajax(data);
}

function setPaging(href, digit, isActive = false) {
    const gray = (href === '' && isNaN(digit)) ? 'gray' : '';
    const active = isActive ? 'active' : '';
    const anchor = `<a class="page-link ${gray}" ${href}>${digit}</a>`;
    return `<li class="page-item ${active}">${anchor}</li>`;
}

function generatePagination(data) {
    let output = '';
    //이전 버튼 
    let prevHref = data.page >1 ? `href=javascript:go(${data.page -1})` : "";
    output += setPaging(prevHref, '이전&nbsp;');

    //페이지 번호
    for (let i = data.startpage; i <= data.endpage; i++){
        const isActive = (i === data.page);
        let pageHref = !isActive ? `href=javascript:go(${i})` : "";
        output += setPaging(pageHref, i, isActive);
    }
    let nextHref = (data.page < data.maxpage) ? `href=javascript:go(${data.page+1})` : "";
    output += setPaging(nextHref,'&nbsp;다음&nbsp;');
    console.log(output);
    $('.pagination').empty().append(output);
}

function ajax(data) {

    console.log("AJAX 요청 데이터:", data);

    $.ajax({
        type:"POST",
        data: data,
        url: 'qna_list_ajax',
        beforeSend : function(xhr)
        {   //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        success: function (data) {
            console.log("서버 응답:", data);  // 서버 응답을 확인
            $('#viewcount').val(data.limit);
            $(".listcount").text('글 개수 : ' + data.listcount);
            if (data.listcount > 0) {
                $('tbody').remove();
                updateBoardList(data); // 게시판 내용 업데이트
                generatePagination(data); // 페이지네이션 생성
            }
        },
        error: function () {
            console.log('에러');
        }
    })
}

function updateBoardList(data) {
    const {listcount, page, limit, qnaList} = data;

    let num = listcount - (page - 1) * limit;
    let output = '<tbody>';

    $(qnaList).each(function (index, item) {

        const {qnaId, qnaSubject, studentName, courseName, className ,teacherName, createdAt, qnaReadCount, qnaExposureStat, qnaAnswerStatus} = item;
        const subject = qnaSubject.length >= 20 ? qnaSubject.substring(0, 20) + "..." : qnaSubject;
        const changeSubject = subject
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt');

        let exposureStatus = item.qnaExposureStat === 1
            ? '<span class="badge bg-primary">노출</span>'
            : '<span class="badge bg-secondary">숨김</span>';

        let answer = item.qnaAnswerStatus === 1
            ? '<span class="badge bg-primary">답변 완료</span>'
            : '<span class="badge bg-secondary">답변 대기</span>';

        output += `
            <tr>
                <td>${num--}</td>
                <td><div><a href='qna/detail?id=${qnaId}'>${changeSubject}</a></a></div></td>
                <td><div>${studentName}</div></td>
                <td><div>${courseName}</div></td>
                <td><div>${className}</div></td>
                <td><div>${teacherName}</div></td>
                <td><div></div>${createdAt.substring(0, 10)}</td>
                <td><div>${qnaReadCount}</div></td>
                <td><div>${exposureStatus}</div></td>
                <td><div>${answer}</div></td>
            </tr>
        `;
    });
    output += '</tbody>';
    $('table').append(output);
}

$(function () {

    console.log("token")
    $('#viewcount').change(function () {
        go(1)
    })
})