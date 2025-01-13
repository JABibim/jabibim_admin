let page = 1;
let useStatus = '';
let searchCondition = 0;
let searchKeyword = '';
let startDate = '';
let endDate = '';



function go(page) {
    getCourseList({useStatus, searchCondition, searchKeyword, startDate, endDate, page});
}

function getCourseList({useStatus, searchCondition, searchKeyword, startDate, endDate, page}) {
    console.log('==> useStatus : ', useStatus);
    console.log('==> searchCondition : ', searchCondition);
    console.log('==> searchKeyword : ', searchKeyword);
    console.log('==> startDate : ', startDate);
    console.log('==> endDate : ', endDate);
    console.log('==> page : ', page);

    const search = {
        useStatus,
        searchCondition,
        searchKeyword,
        startDate,
        endDate,
        page
    }

    $.ajax({
        data: {
            search: JSON.stringify(search)
        },
        url: 'content/getCourseList',
        dataType: 'json',
        cache: false,
        success: function (data) {
            console.log('==> data : ', data);

            const {courseList, courseListCount} = data;
            $('#courseListCount').text(courseListCount);

            $('tbody').remove();
            updateCourseList(data);
            generatePagination(data);

            let newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
            window.history.pushState({path: newUrl}, '', newUrl);
        }, error: function () {
            console.log('과정 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}

function updateCourseList(data) {
    const {courseList, courseListCount} = data;

    let output = '<tbody>';

    $(courseList).each(function (index, item) {
        const {rownum, course_id, course_name, class_count, teacher_name, created_at, is_active} = item;

        const date = new Date(created_at);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day}`;

        output += `
            <tr>
                <th scope="row" style="width: 10%">${rownum}</th>
                <td style="width: 35%">
                   <a href="${contextPath}/content/detail?id=${course_id}">${course_name}</a>
                </td>
                <td style="width: 10%">${class_count}</td>
                <td style="width: 15%">${teacher_name}</td>
                <td style="width: 20%">${formattedDate}</td>
                <td style="width: 10%">
                    <div class="form-check form-switch">
                        <input class="form-check-input toggle-course-status" type="checkbox" id="flexSwitchCheck${course_id}"
                               data-course-id="${course_id}" ${is_active == 1 ? 'checked' : ''}>
                    </div>
                </td>
            </tr>
        `;
    })
    output += '</tbody>';

    $('table').append(output);
}

function generatePagination(data) {
    let output = '';
    const {courseListCount, page, startPage, endPage, maxPage} = data;

    if (courseListCount <= 0) {
        output = '<tr class="text-center"><td colspan="6">데이터가 존재하지 않습니다.</td></tr>';
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

function updateCourseActive(courseId, isActive) {
    $.ajax({
        url: 'content/updateCourseActive',
        type: 'post',
        dataType: 'json',
        data: {courseId, isActive},
        success: (res) => {
            console.log('과정의 활성화 여부 업데이트가 정상적으로 처리되었습니다.');
        },
        error: (err) => {
            console.error('과정의 활성화 여부를 업데이트 하는 도중 오류가 발생했습니다.', err);
        }
    })
}

function validCheck() {
    for (let i = 0; i < arguments.length; i++) {
        let value = arguments[i].trim();

        if (value === null || value === '') {
            showToast('검색하려는 값을 입력해주세요.');
            return false;
        }
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

function validateDates(startDate, endDate) {
    // Date 객체로 변환
    const start = new Date(startDate);
    const end = new Date(endDate);

    // startDate가 유효한지 확인
    if (isNaN(start.getTime())) {
        showToast('시작 날짜가 유효하지 않습니다.');
        return false;
    }

    // endDate가 유효한지 확인
    if (isNaN(end.getTime())) {
        showToast('종료 날짜가 유효하지 않습니다.');
        return false;
    }

    // endDate가 startDate보다 미래인지 확인
    if (end <= start) {
        showToast('종료 날짜는 시작 날짜보다 이후 일자여야 합니다.');
        return false;
    }

    return true;
}

$(function () {
    console.log('================ contentList.js 실행 ===============');

    // 검색 항목을 바꿀때마다 전역변수에 검색 조건을 셋팅하는 로직
    $('.form-select').change(function () {
        searchKeyword = '';
        startDate = '';
        endDate = '';

        $('#disabledSearchInput').remove();
        $('#searchKeyword').remove();
        $('#searchDate').remove();

        searchCondition = $(this).val();
        if (searchCondition === '0') {
            $('#searchInput').append(`<input id="disabledSearchInput" type="text" class="form-control" value="️검색 유형을 선택해주세요." disabled>`);
        } else if (searchCondition === '1' || searchCondition === '2') {
            $('#searchInput').append(`<input type="text" class="form-control" id="searchKeyword" placeholder="검색어를 입력해주세요.">`);
        } else if (searchCondition === '3') {
            $('#searchInput').append(`<div id="searchDate"
                                     style="display: flex; justify-content: space-between; align-items: center;">
                                    <input type="date" class="form-control" id="startDate">
                                    &nbsp;&nbsp;~&nbsp;&nbsp;
                                    <input type="date" class="form-control" id="endDate">
                                </div>`)
        }
    })

    // 이벤트 위임을 사용하여 .toggle-course-status에 이벤트 핸들러를 바인딩
    $('table').on('change', '.toggle-course-status', function () {
        let courseId = $(this).data('course-id'); // COURSE TABLE의 PK
        let isActive = $(this).is(':checked') ? 1 : 0; // 해당 과정의 활성화 여부

        updateCourseActive(courseId, isActive);
    });


    $('#addCourseBtn').click(function () {
        location.href = 'content/addCourse';
    })

    $('#searchBtn').click(function () {
        useStatus = $('input:radio:checked').val();
        console.log('==> useStatus : ', useStatus);
        console.log('==> searchCondition : ', searchCondition);


        if (+searchCondition === 0) {
            console.log('==> 0')
            searchKeyword = '';
        } else if (+searchCondition === 1 || +searchCondition === 2) {
            console.log('==> 1, 2')
            searchKeyword = $('#searchKeyword').val();

            if (!validCheck(searchKeyword)) {
                return;
            }
        } else if (+searchCondition === 3) {
            console.log('==> 3')
            startDate = $('#startDate').val();
            endDate = $('#endDate').val();
            console.log('==> startDate : ', startDate);
            console.log('==> endDate : ', endDate);

            if (!validCheck(startDate, endDate)) {
                return;
            }

            if (!validateDates(startDate, endDate)) {
                return;
            }
        }

        getCourseList({
            useStatus, searchCondition, searchKeyword, startDate, endDate, page
        })
    })
})