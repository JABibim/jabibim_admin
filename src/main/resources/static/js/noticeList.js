
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
        url: 'list_ajax',
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
    const {listcount, page, limit, noticeList} = data;

    let num = listcount - (page - 1) * limit;
    let output = '<tbody>';

    $(noticeList).each(function (index, item) {

        const {boardSubject, teacherName, createdAt, boardReadCount, boardExposureStat, boardNotice} = item;
        const subject = boardSubject.length >= 20 ? boardSubject.substring(0, 20) + "..." : boardSubject;
        const changeSubject = subject
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt');

        let exposureStatus = item.boardExposureStat === 1
            ? '<span class="badge bg-primary">노출</span>'
            : '<span class="badge bg-secondary">숨김</span>';

        let notice = item.boardNotice === 1
            ? '<span class="badge bg-primary">고정</span>'
            : '<span class="badge bg-secondary">비고정</span>';

        output += `
            <tr>
                <td>${num--}</td>
                <td><div><a href='detail?subject=${boardSubject}'>${changeSubject}</a></a></div></td>
                <td><div>${teacherName}</div></td>
                <td><div></div>${createdAt.substring(0, 10)}</td>
                <td><div>${boardReadCount}</div></td>
                <td><div>${exposureStatus}</div></td>
                <td><div>${notice}</div></td>
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