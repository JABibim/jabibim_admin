
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
        url: 'list_ajax2',
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
    const {listcount, page, limit, servicelist} = data;

    let num = listcount - (page - 1) * limit;
    let output = '<tbody>';

    $(servicelist).each(function (index, item) {

        const {rnum, serviceTermSubject, serviceTermName, serviceTermEffectiveDate} = item;
        const subject = serviceTermSubject.length >= 20 ? serviceTermSubject.substring(0, 20) + "..." : serviceTermSubject;
        const changeSubject = subject
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt');

        output += `
            <tr>
                <td>${num--}</td>
                <td style="text-align: center"><div><a href='service/detail?rnum=${rnum}'>${changeSubject}</a></a></div></td>
                <td style="text-align: center"><div>${serviceTermName}</div></td>
                <td style="text-align: center"><div>${serviceTermEffectiveDate}</div></td>
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