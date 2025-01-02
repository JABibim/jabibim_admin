const page = 1;

function getStudentChartData() {
    $.ajax({
        url: 'student/getStudentChartData',
        dataType: 'json',
        cache: false,
        data: {},
        success: function (res) {
            const dateList = res.dateList;
            const studentCntList = res.studentCntList;

            // 차트 옵션값입니다.
            Apex.chart = {
                locales: [{
                    name: 'ko',
                    options: {
                        months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                        shortMonths: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                        days: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
                        shortDays: ['일', '월', '화', '수', '목', '금', '토'],
                        toolbar: {
                            exportToSVG: 'SVG로 내보내기',
                            exportToPNG: 'PNG로 내보내기',
                            menu: '메뉴'
                        }
                    }
                }],
                defaultLocale: 'ko'
            };
            new ApexCharts(document.querySelector("#studentChart"), {
                series: [{
                    name: "학생 수",
                    data: studentCntList
                }],
                chart: {
                    type: 'area',
                    height: 350,
                    zoom: {
                        enabled: false
                    }
                },
                xaxis: {
                    type: 'datetime',
                    categories: dateList, // 날짜 리스트 (ISO 8601 형식이 권장됨, 예: "2024-11-22")
                    labels: {
                        formatter: function (value) {
                            const date = new Date(value);
                            const year = (date.getFullYear() % 100).toString().padStart(2, '0'); // 24년
                            const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 11월
                            const day = date.getDate().toString().padStart(2, '0'); // 22일
                            // return `${year}년 ${month}월 ${day}일`;
                            return `${month}월 ${day}일`;
                        }
                    }
                },
                yaxis: {
                    title: {
                        text: "학생 수"
                    }
                }
            }).render();
        },
        error: function (err) {
            console.log('학생 증가 추이를 표현하는 차트를 그리는데 오류가 발생했습니다.')
        },
    })
}

function go(page) {
    getDashboardSystemNoticeList(page);
}

function getDashboardSystemNoticeList(page) {
    $.ajax({
        url: 'setting/getDashboardSystemNoticeList',
        dataType: 'json',
        cache: false,
        data: {page},
        success: function (res) {
            $('#systemBoard tbody').empty();

            updateSystemNoticeList(res);
            generatePagination(res);
        },
        error: function (err) {
            console.log('시스템 공지사항 리스트를 가져오는 도중에 오류가 발생했습니다.')
        },
    })
}

function updateSystemNoticeList(data) {
    const {systemNoticeList, systemNoticeListCount} = data;

    let output = '<tbody>';

    $(systemNoticeList).each(function (index, item) {
        const {system_notice_id, system_notice_title, system_notice_category, created_at} = item;

        const date = new Date(created_at);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day}`;

        output += `
            <tr>
                <td>${system_notice_category}</td>
                <td class="notice" style="color: blue; cursor: pointer"
                    data-system-notice-id="${system_notice_id}" data-bs-toggle="modal"
                    data-bs-target="#verticalycentered">${system_notice_title}</td>
                <td>${formattedDate}</td>
            </tr>
        `;
    })
    output += '</tbody>';

    $('#systemBoard').append(output);
}

function generatePagination(data) {
    let output = '';
    const {systemNoticeListCount, page, startPage, endPage, maxPage} = data;

    if (systemNoticeListCount <= 0) {
        output = '<tr style="text-align: center"><td colspan="3">데이터가 존재하지 않습니다.</td></tr>';
    }

    // 이전 버튼
    let prevHref = page > 1 ? `href=javascript:go(${page - 1})` : '';
    output += setPaging(prevHref, '이전');

    // 페이지 번호
    for (let i = startPage; i <= endPage; i++) {
        let isActive = i === page; // 활성화된 버튼
        let pageHref = !isActive ? `href=javascript:go(${i})` : '';

        output += setPaging(pageHref, i, isActive);
    }

    // 다음 버튼
    let nextHref = page < maxPage ? `href=javascript:go(${page + 1})` : '';
    output += setPaging(nextHref, '다음');

    $('.pagination').empty().append(output);
}

function setPaging(href, digit, isActive = false) {
    const gray = (href === '' && isNaN(digit)) ? 'disabled' : '';
    const active = isActive ? 'active' : '';
    const anchor = `<a class="page-link ${gray}" ${href}>${digit}</a>`;

    return `<li class="page-item ${active}">${anchor}</li>`;
}

function getSystemNoticeDetail(systemNoticeId) {
    $.ajax({
        url: 'board/getSystemNoticeDetail',
        dataType: 'json',
        cache: false,
        data: {systemNoticeId},
        success: function (res) {
            const {
                system_notice_title,
                system_notice_content,
                system_notice_category,
                created_at
            } = res.systemNoticeDetail;

            const formattedDate = new Date(created_at).toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });

            $('#verticalycentered .modal-title').text(system_notice_title); // 제목
            $('#verticalycentered .modal-body').html(`
                <p><strong>카테고리:</strong> ${system_notice_category}</p>
                <p><strong>작성일:</strong> ${formattedDate}</p>
                <p>${system_notice_content}</p>
            `);

            // 모달 표시
            $('#verticalycentered').modal('show');
        },
        error: function (err) {
            console.log('시스템 공지사항 상세정보를 가져오는 도중에 오류가 발생했습니다.')
        },
    })
}

$(function () {
    // getDashboardSystemNoticeList();
    // getStudentChartData();

    $('.refundDetailBtn').click(function () {
        window.location = contextPath + '/orders/refund';
    })

    $('.courseDetailBtn').click(function () {
        window.location = contextPath + '/content';
    })

    $('.studentDetailBtn').click(function () {
        window.location = contextPath + '/student';
    })

    $(document).on('click', '.notice', function () {
        let systemNoticeId = $(this).data('system-notice-id');

        getSystemNoticeDetail(systemNoticeId);
    })
})