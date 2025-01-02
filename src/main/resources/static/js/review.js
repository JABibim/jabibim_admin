// Ajax.History 객체 정의
const Ajax = {};
Ajax.History = {
    initialize: function (options) {        // AJAX.Hitory 객체 초기화
        this.options = Object.assign({      // assign() 메서드 공부 필요
            interval: 200
        }, options || {});
        this.callback = this.options.callback || (() => {       // URL 체크해서 다르면 호출할 callback 함수 정의
        });
        if (navigator.userAgent.toLowerCase().indexOf('msie') > 0) {  // ie , 익스플로러 환경에서 쓰이는 객체
            this.locator = new Ajax.History.Iframe('ajaxHistoryHandler', this.options.iframeSrc);
        } else {
            this.locator = new Ajax.History.Hash();  // 크롬, 파이어 폭스 등 환경에서 쓰이는 History 기능 이용
        }
        this.currentHash = '';          // 현재 URL 을 '' 로 초기화
        this.locked = false;            // locked = false 이면 200ms 마다 URL 체크
    },
    add: function (hash) {      //
        this.locked = true;             // URL 체크 멈추고
        clearTimeout(this.timer);       // 인터벌 지우고
        this.currentHash = hash;        // 입력 받은 hash 데이터(에이젝스 호출에 필요한 파라미터를 JSON 으로 파싱하고 해싱
        this.locator.setHash(hash);     // URL 에 해시 값을 입력
        this.timer = setTimeout(this.checkHash.bind(this), this.options.interval);  // 다시 인터벌
        this.locked = false;            // URL 체크 시작
        console.log(hash);
    },
    checkHash: function () {        
        if (!this.locked) {                     // locked false 면
            var check = this.locator.getHash();     // 현재 URL 가져오고
            if (check !== this.currentHash) {       // 저장된 URL 과 다르면
                this.callback(check);               // callback 함수 호출하여 (URL 에 담긴 hash 정보로 에이젝스 실행)
                this.currentHash = check;           // 페이지 로드
            }
        }
        this.timer = setTimeout(this.checkHash.bind(this), this.options.interval);      // 기본 200ms
    },
    getBookmark: function () {
        return this.locator.getBookmark();              // 해싱된 URL 에서 현재 페이지에 해당하는 부분 가져오기
    }
};
// 최신 브라우저용 Hash Handler 클래스 정의, Window 객체에 대해 공부 필요
Ajax.History.Hash = class {
    constructor() {
    }

    setHash(hash) {
        window.location.hash = hash;                    // URL에 정보 추가
    }

    getHash() {
        return window.location.hash.substring(1) || '';     // URL에서 정보 가져오기
    }

    getBookmark() {
        try {
            return window.location.hash.substring(1) || '';     // URL에서 정보 가져오기
        } catch (e) {
            return '';
        }
    }
};

// 2024-01-24 같은 문자열을 Date 타입으로 바꾼 후 파라미터로 넘기면 YYYY-MM-DD 형태의 Date 로 반환해준다.
// 기본 형태는 OS 와 Timezone 마다 다르지만 Thu May 19 2022 18:11:13 이런 형태로 생성된다.
// 이것을 2022-05-19 같은 형태로 바꾸어 input type="date" 에 val()로 넣어 준다.
function dateFormat(date) {
    console.log(date);
    return date.getFullYear() +
        '-' + ((date.getMonth() + 1) < 10 ? "0".concat(date.getMonth() + 1) : (date.getMonth()) + 1) +
        '-' + ((date.getDate()) < 10 ? "0".concat(date.getDate()) : (date.getDate()));
}

function reviewListAjax(page, limit, search) {

    let hashParams = encodeURIComponent(JSON.stringify({page: page, limit: limit, search: search}));
    Ajax.History.add(hashParams);  // 히스토리에 현재 상태 추가

    $.ajax({
        url: "reviewlist",
        type: "POST",
        cache: false,
        data: {
            "page": page,
            "limit": limit,
            "search": search
        },
        dataType: "json",
        success: function (data) {
            console.log(data);
            let $limit = data.limit;
            let $listCount = data.listCount;
            let $page = data.page;
            let $maxPage = data.maxPage;
            let $startPage = data.startPage;
            let $endPage = data.endPage;

            let $search = data.search;

            let $form_start = '';
            let $form_end = '';
            let $form_status = '0';
            let $form_rating = '0';
            let $form_visible = '0';
            let $form_field = '0';
            let $form_word = '';

            // 검색 폼 조정
            if ($search) {
                console.log($search);


                if ($search.reviewTimeStart != null && $search.reviewTimeStart !== '') {
                    $('#reviewDate1').val(dateFormat(new Date($search.reviewTimeStart)));
                    $form_start = $search.reviewTimeStart;
                }
                if ($search.reviewTimeEnd != null && $search.reviewTimeEnd !== '') {
                    $('#reviewDate2').val(dateFormat(new Date($search.reviewTimeEnd)));
                    $form_end = $search.reviewTimeEnd;
                }


                $('input[name=review_status]').each(function () {
                    if ($search.review_status === $(this).val()) {
                        $(this).prop('checked', true);
                    }
                });
                $form_status = $search.review_status;

                $('input[name=rating]').each(function () {
                    if ($search.rating === $(this).val()) {
                        $(this).prop('checked', true);
                    }
                });
                $form_rating = $search.rating;

                $('input[name=review_visible]').each(function () {
                    if ($search.review_visible === $(this).val()) {
                        $(this).prop('checked', true);
                    }
                });
                $form_visible = $search.review_visible;

                $('#main > div.container-fluid > div > div > div > div.form_container.container > form > div:nth-child(4) > div.input-group.align-self-center > div.form-group.col-3.col-md-auto.mx-2 > select option').each(function () {
                    if ($(this).val() === $search.search_field) {
                        $(this).prop('selected', "selected");
                    }
                })
                $form_field = $search.search_field

                if ($search.word !== null && $search.word !== '' && $search.word !== undefined) {
                    $('#search_word').val($search.word);
                    $form_word = $('#search_word').val();
                }

            }

            // 총 게시물 수 출력 및 limit 값 조정
            $('#main > div.container-fluid > div > div > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)').text('전체: ' + $listCount + ' 건');

            $('#itemsPerPage option').each(function () {
                $(this).prop('selected', $(this).val() == $limit ? 'selected' : '');
            });
            
            // detail 출력 객체 삭제
            $('#main > div.container-fluid').children().not('#review-card').remove();
            $('#review-card').show();

            $('#reviewlist-body').empty();
            $('#main > div.container-fluid > div > div > div > div:nth-child(2) > div:nth-child(3)').empty();

            if (data.list.length == 0) {
                let out = `  <tr class="text-center">
                                        <td colspan="10">데이터가 존재하지 않습니다.</td>
                                    </tr>`
                $('#reviewlist-body').append(out);
            } else {

                // 테이블 행 추가
                data.list.forEach(function (item, index) {

                    if (item.board_title.length > 20) {
                        item.board_title = item.board_title.substring(0, 17) + '...';
                    }
                    let output = `<tr>
                                        <td>${index + 1 + (data.page - 1) * data.limit}</td>
                                        <td>${item.board_title}  <span style="color:orange">[${item.cnt}]</span></td>
                                        <td>${item.student_name}</td>
                                        <td>${item.student_email}</td> 
                                        <td>`;
                    for (var j = 1; j <= 5; j++) {
                        if (item.board_rating < j * 10 - 5) {
                            output += `<i class="bi bi-star rating"></i>`;
                        } else if (item.board_rating < j * 10) {
                            output += `<i class="bi bi-star-half rating"></i>`;
                        } else {
                            output += `<i class="bi bi-star-fill rating"></i>`;
                        }
                    }
                    output += `</td>
                                        <td>${item.created_at}</td>
                                        <td>${item.teacher_name}</td>
                                        <td>${item.cnt >= 1 ? '<span class="badge rounded-pill bg-primary">답변 완료</span>' :
                        '<span class="badge rounded-pill bg-warning">답변 대기</span>'}</td>
                                        <td style="width: 10%">
                                            <div class="form-check form-switch">
                                                <input class="form-check-input toggle-exposure-status" type="checkbox" id="${item.board_id}" ${item.board_exposure_status === 1 ? 'checked="checked"' : ''}>
                                            </div>
                                        </td>
                                    </tr>`;
                    $('#reviewlist-body').append(output);
                });

                // 페이지네이션 구현

                // 페이지 출력을 위한 파라미터를 json 객체로 만들어본다.
                let param = {
                    "reviewTimeStart": $form_start,
                    "reviewTimeEnd": $form_end,
                    "review_status": $form_status,
                    "rating": $form_rating,
                    "review_visible": $form_visible,
                    "search_field": $form_field,
                    "search_word": $form_word
                };
                var search = JSON.stringify(param);

                var pagination = `<ul class="pagination justify-content-center">
                                        <li class='page-item fw-bold'>
                                            <a href='javascript:void(0)' ${$page <= 1 ? "class='page-link disabled' style='color:gray'" : "class='page-link'"} id='${$page - 1}&${$limit}&${search}'><span>&lt;</span>이전</a>
                                        </li>`;


                for (var i = $startPage; i <= $endPage; i++) {
                    pagination += `<li class="page-item${i == $page ? ' active' : ''}">
                                    <a href='javascript:void(0)' class="page-link" id='${i}&${$limit}&${search}'> ${i} </a>
                               </li>`;
                }

                pagination += `<li class="page-item fw-bold">
                                <a href='javascript:void(0)' ${$page >= $maxPage ? "class='page-link disabled' style='color:gray'" : "class='page-link'"} id= '${$page + 1}&${$limit}&${search}'>다음<span>&gt;</span></a> 
                           </li>
                        </ul>`;

                $('#main > div.container-fluid > div > div > div > div:nth-child(2) > div:nth-child(3)').append(pagination);
            }

        }//success end
        , error: function (err) {
            console.log('============> err');
            console.log(err);

        }
    }); // ajax end
} // reviewListAjax end

function getReviewDetail(board_id) {
    let hashParams = encodeURIComponent(JSON.stringify({board_id}));
    Ajax.History.add(hashParams);  // 히스토리에 현재 상태 추가

    $.ajax({
        url: "getreviewdetail",
        type: "POST",
        cache: false,
        data: {"board_id": board_id},
        dataType: "html",
        success: function (data) {
            const $body = $('#main > div.container-fluid');
            $('#review-card').hide();
            $('#review-card').siblings().remove();
            $body.append(data);
            reviewDetailQuill();
        }
    })
}

function updateReviewExposure(board_id, isActive) {
    $.ajax({
        url: "updatereviewexposure",
        type: "POST",
        cache: false,
        data: {
            "board_id": board_id,
            "is_active": isActive
        },
        dataType: "json",
        success: function (data) {
            console.log(data);
            if (data.result == 1) {
                alert("공개 여부 상태가 변경되었습니다.");
            } else {
                alert("공개 여부 상태 변경에 실패했습니다.");
                $('"#' + data.board_id + '"').prop('checked', data.is_active == 1 ? false : true);
            }
        }
    });
}

function replyAjax(board_id, board_title, board_content, student_id, created_at, origin_id) {

    $.ajax({
        url: "replyupdate",
        type: "POST",
        cache: false,
        data: {
            "board_id": board_id,
            "board_title": board_title,
            "board_content": board_content,
            "student_id": student_id,
            "created_at": created_at
        },
        dataType: "json",
        success: function (data) {
            if (data.result === 1) {
                alert("작성이 완료되었습니다.");
            } else {
                alert('작성에 실패했습니다.');
            }
        },
        complete: function () {
            if (origin_id) {
                getReviewDetail(origin_id);
            } else {
                getReviewDetail(board_id);
            }
        }
    })
}

function getSearch() {
    let reviewTimeStart = $('#reviewDate1').val().length === 0 ? '' : $('#reviewDate1').val();
    let reviewTimeEnd = $('#reviewDate2').val().length === 0 ? '' : $('#reviewDate2').val();

    console.log(reviewTimeStart + reviewTimeEnd);

    let review_status = '0';
    $('input[name="review_status"]').each(function () {
        if ($(this).is(":checked")) {
            review_status = $(this).val();
        }
    });
    let rating = '0';
    $('input[name="rating"]').each(function () {
        if ($(this).is(":checked")) {
            rating = $(this).val();
        }
    });

    let review_visible = '0'
    $('input[name="review_visible"]').each(function () {
        if ($(this).is(":checked")) {
            review_visible = $(this).val();
        }
    });

    let review_searchField = $('select[name="review_searchField"]').val();

    let search_word = '';
    if ($('#search_word').val() != null && $('#search_word').val() !== '') {
        search_word = $('#search_word').val();
    }

    let obj = {
        "reviewTimeStart": reviewTimeStart,
        "reviewTimeEnd": reviewTimeEnd,
        "review_status": review_status,
        "rating": rating,
        "review_visible": review_visible,
        "search_field": review_searchField,
        "search_word": search_word
    }

    return JSON.stringify(obj);
}

function reviewDetailQuill() {
    // #review_content에 Quill이 이미 존재하는지 확인
    if (!$('#review_content').data('quill')) {
        const quill_review_content = new Quill('#review_content', {
            readOnly: true,
            modules: {
                toolbar: null
            },
            theme: 'snow'
        });
        // Quill 인스턴스를 data 속성에 저장
        $('#review_content').data('quill', quill_review_content);
    }

    // #reply_content에 Quill이 이미 존재하는지 확인
    if ($('#reply_content').length && !$('#reply_content').data('quill')) {
        const quill_reply_content = new Quill('#reply_content', {
            readOnly: true,
            modules: {
                toolbar: null
            },
            theme: 'snow'
        });
        // Quill 인스턴스를 data 속성에 저장
        $('#reply_content').data('quill', quill_reply_content);
    }

    // #review_editor에 Quill이 이미 존재하는지 확인
    if ($('#review_editor').length && !$('#review_editor').data('quill')) {
        const toolbarOptions = [
            [{
                font: []
            }, {
                size: []
            }],
            ["bold", "italic", "underline", "strike"],
            [{
                color: []
            },
                {
                    background: []
                }
            ],
            [{
                script: "super"
            },
                {
                    script: "sub"
                }
            ],
            [{
                list: "ordered"
            },
                {
                    list: "bullet"
                },
                {
                    indent: "-1"
                },
                {
                    indent: "+1"
                }
            ],
            ["direction", {
                align: []
            }],
            ["link"],
            ["clean"]
        ];

        const quill_editor = new Quill('#review_editor', {
            modules: {
                toolbar: toolbarOptions
            },
            theme: 'snow'
        });
        // Quill 인스턴스를 data 속성에 저장
        $('#review_editor').data('quill', quill_editor);
    }
}

$(function () {

    // Ajax.History 초기화
    Ajax.History.initialize({
        interval: 200,  // 해시 변경 감지 주기
        callback: function (newHash) {
            let params = JSON.parse(decodeURIComponent(newHash));

            // 해시가 변경되면 해당 페이지와 필터 정보를 다시 불러오기

            if (params.board_id) {
                console.log(params.board_id);
                getReviewDetail(params.board_id);
            } else {
                console.log(params.page, params.limit, params.search);
                $('#review-card').show();
                reviewListAjax(params.page, params.limit, params.search);
            }
        }
    });

    // 초기 로딩
    let initialSearch = getSearch();
    reviewListAjax(1, 10, initialSearch);


    $('#main > div.container-fluid > div > div > div > div:nth-child(2) > div:nth-child(1)').on('change', '#itemsPerPage', function (e) {
        const limit = $(this).val();
        let search = getSearch();

        reviewListAjax(1, limit, search);
    });// limit 변경시 end

    $(document).on('click', '#main > div.container-fluid > div > div > div > div.form_container.container > form > div:nth-child(5) > div.input-group.align-self-center > div.form-group.col-auto.col-md-auto > button', function (e) {
        e.preventDefault();

        let limit = $('#itemsPerPage').val();

        let search = getSearch();
        console.log(limit);
        console.log(search);
        reviewListAjax(1, limit, search);
    });

    $('#main > div.container-fluid > div > div > div > div:nth-child(2) > div:nth-child(3)').on('click', '.page-link', function (e) {
        var param = $(this).attr('id');

        param = param.split('&');
        let page = param[0];
        let limit = param[1];
        param = JSON.parse(param[2]);
        console.log(param);
        var obj = {
            "reviewTimeStart": param.reviewTimeStart,
            "reviewTimeEnd": param.reviewTimeEnd,
            "review_status": param.review_status,
            "rating": param.rating,
            "review_visible": param.review_visible,
            "search_field": param.search_field,
            "search_word": param.search_word == null ? "" : param.search_word
        }

        let search = JSON.stringify(obj);

        reviewListAjax(page, limit, search);

    });


    $('.table').on('change', '.toggle-exposure-status', function () {
        if (!confirm(!$(this).is(':checked') ? '해당 수강평을 숨기시겠습니까?' : '해당 수강평을 공개하시겠습니까?')) {
            $(this).prop('checked', !$(this).is(':checked'));
            return false;
        } else {
            let board_id = $(this).attr('id'); // board_id
            let isActive = $(this).is(':checked') ? 1 : 0; // 해당 과정의 활성화 여부
            console.log(isActive);
            updateReviewExposure(board_id, isActive);
        }

    });

    $('#reviewlist-body').on('click', 'tr td:nth-child(0), tr td:nth-child(1), tr td:nth-child(2), tr td:nth-child(3), tr td:nth-child(4), tr td:nth-child(5), tr td:nth-child(7), tr td:nth-child(7), tr td:nth-child(8)', function (e) {
        let board_id = $(this).parent().find('input[type="checkbox"]').attr('id');

        getReviewDetail(board_id);
    });


    $('#main > div.container-fluid').on('click', '#review_write', function () {
        let board_title = $('#reply_title').val();
        if (board_title === null || board_title.trim() === '') {
            alert("제목을 입력하세요.");
            $('#reply_title').focus();
            return false;
        }

        let board_content = $('#review_editor > .ql-editor').html();
        if (board_content.length === 1 || board_content.trim() === '<p>내용을 입력하세요</p>') {
            alert("내용을 입력하세요.");
            $('#review_write').focus();
            return false;
        }

        let board_id = $('input[name="board_id"]').val();

        let student_id = $('input[name="student_id"]').val();

        let created_at = $('input[name="created_at"]').val();

        let origin_id = $('input[name="origin_id"]').val();

        if (created_at == null && confirm("수강평에 대한 답변을 작성하시겠습니까?")) {
            replyAjax(board_id, board_title, board_content, student_id);

        } else if (created_at != null && confirm("수강평에 대한 답변을 수정하시겠습니까?")) {
            replyAjax(board_id, board_title, board_content, student_id, created_at, origin_id);

        }
    });

    $('#main > div.container-fluid').on('click', '#review_cancel', function () {
        $('#review-card').siblings().remove();
        $('#review-card').show();
    });

    $('#main > div.container-fluid').on('click', '#review_back', function () {
        $('#review-card').siblings().remove();
        $('#review-card').show();
    });

    $('#main > div.container-fluid').on('click', '#update_reply', function () {
        console.log('asdfasdf');
        $('#comment > h5').html('답변 수정');
        let title = $('#comment > div:nth-child(3) > div.col-lg-9.col-md-8').text();
        let $title = $('#comment > div:nth-child(3)');
        let content = $('#reply_content > div').html();
        let $content = $('#comment > div:nth-child(5) > div');
        let board_id = $('input[name="board_id"]').val();
        let student_id = $('input[name="student_id"]').val();
        let created_at = $('#comment > div:nth-child(6) > div.col-lg-9.col-md-8').text();
        let origin_id = $('input[name="origin_id"]').val();

        $title.children('div:eq(1)').remove();
        $content.empty();
        $('#comment > div:nth-child(6), #comment > div:nth-child(7)').remove();

        $title.append(`<div class="col-lg-9 col-md-8"><label for="reply_title"><input type="text" id="reply_title" size="50" value='${title}'></label></div>`);
        $content.append(`<div id="review_editor">${content}</div>`);

        $('#comment').append(
            ` <div class="row mb-3 mt-5">
                            <div class="col-lg-9 col-md-8">
                                <button type="button" class="btn btn-small btn-success" id='review_write'>등록하기</button><input type='hidden' name='board_id' value='${board_id}'/><input type='hidden' name='student_id' value='${student_id}'/><input type='hidden' name='created_at' value='${created_at}'/><input type='hidden' name='origin_id' value='${origin_id}'/>
                                <button type="button" class="btn btn-small btn-danger" id='review_cancel'>취소하기</button>
                            </div>
                        </div>`);

        reviewDetailQuill();

    });

    $('#main > div.container-fluid').on('click', '#delete_reply', function () {
        if (confirm("정말로 답변을 삭제하시겠습니까?")) {
            let board_id = $('input[name="board_id"]').val();
            let origin_id = $('input[name="origin_id"]').val();

            $.ajax({
                url: 'replydelete',
                method: 'POST',
                data: {"board_id": board_id},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.result === 1) {
                        alert("삭제에 성공했습니다.");
                    } else {
                        alert("삭제에 실패했습니다.");
                    }
                },
                complete: function () {
                    getReviewDetail(origin_id);
                }
            });
        }
    });
});