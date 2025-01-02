// 2024-01-24 같은 문자열을 Date 타입으로 바꾼 후 파라미터로 넘기면 YYYY-MM-DD 형태의 Date 로 반환해준다.
// 기본 형태는 OS 와 Timezone 마다 다르지만 Thu May 19 2022 18:11:13 이런 형태로 생성된다.
// 이것을 2022-05-19 같은 형태로 바꾸어 input type="date" 에 val()로 넣어 준다.
function dateFormat(date) {
    console.log(date);
    return date.getFullYear() +
        '-' + ((date.getMonth()+1) < 10 ? "0".concat(date.getMonth()+1) : (date.getMonth())+1) +
        '-' + ((date.getDate()) < 10 ? "0".concat(date.getDate()) : (date.getDate()));
}

// ajax 시작
function signHistoryListAjax(page, limit, dateStart, dateEnd, hist_is_success,signHist_searchField, signHist_searchWord) {
    $.ajax({
        url : "signhistlist",
        type: "post",
        data : {"page": page, // 현재 페이지
            "limit": limit, // 출력 건 수
            "dateStart": dateStart, // 날짜 시작
            "dateEnd": dateEnd, // 날짜 끝
            "success": hist_is_success, // 성공 여부
            "search_field": signHist_searchField, // 검색 필드
            "search_word": signHist_searchWord }, // 검색어
        dataType: "json",
        cache: false,
        success: function(data) {
            // console.log(data); // toString 으로 전달된 json 출력
            // console.log(data.page); // 현재 페이지 값
            // console.log(data.startPage); // 페이지 그룹 시작 값
            // console.log(data.endPage); // 페이지 그룹 끝 값
            // console.log(data.maxPage); // histCount 와 limit 으로 결정되는 마지막 페이지 값
            // console.log(data.histCount); // db에 있는 총 게시물 수 또는 검색 조건에 맞는 게시물 수
            // console.log(data.limit); // 페이지 당 출력할 게시물 수
            // console.log(data.list); // ArrayList 에 담겼던 객체를 array 형태로 받아 forEach 로 for 문 돌릴 수 있다.

            // 상세 검색 폼의 입력값을 저장하기 위한 변수 선언.
            // 상세 검색에 입력값이 없으면 undefined 로 파라미터에 전달되어 무시되게끔 한다.
            let $dateStart = undefined;
            let $dateEnd = undefined;
            let $hist_is_success = undefined;
            let $field = undefined;
            let $word = undefined;

            // 전체 건수 출력
            $('#main > div.container > div > div > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)').text('Total: ' + data.histCount +' 건');

            // limit 값에 맞게 select 태그 수정
            $('#itemsPerPage option').each(function() {
                $(this).prop('selected', $(this).val() == data.limit ? 'selected' : '');
                if($(this).prop('selected') == 'selected') {
                    $('#itemsPerPage').val($(this).val());
                }
            });

            // 상세 검색 폼에 입력된 값을 자바 서블릿에서 HashMap 형태로 다루고
            // json 에 담아 반환했는데 Object 형태로 반환된다. 변수를 선언해서 값을 담고
            const $search = data.search;

            // 반환 받은 Object 데이터 search 가 있으면 데이터에 맞게 상세 검색 폼 수정
            // 없으면 건너 뛴다.
            if ($search) {

                console.log($search); // 데이터 콘솔에 출력해보고 형태를 살펴 본다.


                // 상세검색 폼에 아무것도 입력하지 않았을 때 undefined 또는 null 또는 "null" 로 지정 했으므로
                // 체크해주고 각 input 태그에 val() 로 데이터 삽입


                if ($search.dateStart !== null && $search.dateStart !== "null" && $search.dateStart !== undefined) {
                    $('#sighHistDate1').val(dateFormat(new Date($search.dateStart)));
                    // input type="date" 가 value 값으로 date 타입만 받으므로 YYYY-MM-DD 포맷의 date 타입 객체를 넘긴다.
                    console.log(dateFormat(new Date($search.dateStart)));
                    $dateStart = dateFormat(new Date($search.dateStart));
                    // 이후 페이지네이션에서 사용하기 위해 변수에 값 저장
                }

                if ($search.dateEnd !== null && $search.dateEnd !== "null" && $search.dateEnd !== undefined) {
                    $('#sighHistDate2').val(dateFormat(new Date($search.dateEnd)));
                    // input type="date" 가 value 값으로 date 타입만 받으므로 YYYY-MM-DD 포맷의 date 타입 객체를 넘긴다.
                    console.log(dateFormat(new Date($search.dateEnd)));
                     $dateEnd = dateFormat(new Date($search.dateEnd));
                    // 이후 페이지네이션에서 사용하기 위해 변수에 값 저장
                }

                // input type="radio" 태그에 설정했던 데이터를 그대로 지정하기 위해 each 함수를 이용해 값이 맞는 radio 에 checked 값 넣어 준다.
                // input type="checkbox" 의 경우는 다른 코딩이 필요하다.
                $('input[name=is_success]').each(function(){
                        if($search.is_success === $(this).val()) {
                            $(this).prop('checked', true);
                        }
                });

                // 이후 페이지네이션에서 사용하기 위해 변수에 값 저장
                $hist_is_success = $search.is_success;



                // 검색 대상을 입력하는 select 태그의 자식인 option 태그 객체들을 each 함수로 돌며 지정된 field 값에 맞게 selected 요소를 추가한다.
                $('#main > div.container > div > div > div > div:nth-child(1) > form > div:nth-child(3) > div.input-group.align-self-center > div.form-group.col-3.col-md-auto.mx-2 > select').find('option').each(function() {
                    if ($(this).val() === $search.field) {
                        $(this).prop('selected', "selected");
                    }
                })

                // 이후 페이지네이션에서 사용하기 위해 변수에 값 저장
                $field = $search.field;

                // 지정된 검색어가 null , "null", undefined 가 아니면 검색창에 지정된 값을 삽입하고 변수에 저장한다.
                if ($search.word !== null && $search.word !== "null" && $search.word !== undefined) {
                  $('#search_word').val($search.word);
                  $word = $search.word;
                }

            } // form 설정 end

            if (data.list.length != 0) { // 게시글이 있을 때

                $('#signHistTbody').empty(); // 테이블 비우기
                $('#main > div.container > div > div > div > div:nth-child(2) > div:nth-child(3)').empty(); // 페이지네이션 비우기

                data.list.forEach(function (item, index) { // array 객체에 forEach 로 for 문 시작
                    var input = `<tr> 
                                      <td> ${index + 1 + (data.page - 1) * data.limit} </td> // 글 번호 => 페이지 * limit 만큼 인덱스에 더해준다. 
                                      <td> ${item.student_email}</td>                        // 이후 적절한 데이터 삽입
                                      <td> ${item.is_login_success == 1 ? '성공' : '실패'}</td>  
                                      <td> ${item.login_hist_browser}</td>      
                                      <td> ${item.login_hist_os}</td>
                                      <td> ${item.login_hist_ip}</td>
                                      <td> ${item.created_at}</td>
                                </tr>`;

                    $('#signHistTbody').append(input);
                    // 테이블에 추가
                });
                // list forEach end

                // limit 선언
                const $limit =$('#itemsPerPage').val();


                // 페이지네이션 시작

                // 이전 버튼
                // 백틱 사용 시 편하게 자바스크립트 변수를 사용할 수 있지만 헷갈리는 부분이 많으므로
                // f12 에 출력되는 값을 살펴보며 입력해야 한다.
                var pagination =`<ul class='pagination justify-content-center'>
                                          <li class='page-item fw-bold'>
                                            <a href=${data.page > 1 ? 'javascript:signHistoryListAjax('+(data.page-1)+','+ $limit +',"' +  $('#sighHistDate1').val() + '","' +$('#signHistDate2').val()  + '",' + $hist_is_success + ','+ $field + ',"' + $word + '")' : '#'}
                                                ${data.page <= 1 ? "class='page-link disabled' style='color:gray'" : 'class="page-link"'}>이전</a>
                                          </li>`;
                                          // href=javascript:함수(파라미터) 로 ajax 함수를 호출하는 방식
                                          // href=javascript:void(0) 으로 설정하고 js 에 따로 on('click', '선택자', function(){}) 을 이용하여
                                          // 실행시키는게 더 편할것 같다. hidden 태그로 ajax 호출에 필요한 파라미터를 입력하면 백틱 때문에 고생할 일도 줄어들 듯

                // 페이지 버튼
                // 페이지 그룹 시작부터 끝까지 for 문으로 생성
                for (var cnt = data.startPage; cnt <= data.endPage; cnt++) {
                    pagination += `<li class='page-item${cnt == data.page ? ' active' : ''}'>
                                     <a href='javascript:signHistoryListAjax(${cnt+','+ $limit +',"' +  $('#sighHistDate1').val() + '","' +$('#signHistDate2').val() + '",' + $hist_is_success + ','+ $field + ',"' + $word}")' class='page-link'> ${cnt} </a>
                                   </li>`;
                }
                
                // 다음 버튼
                pagination += `<li class='page-item fw-bold'>
                                   <a href=${data.page < data.maxPage ? 'javascript:signHistoryListAjax('+(data.page+1)+','+ $limit +',"' +  $('#sighHistDate1').val() + '","' + $('#signHistDate2').val()  + '",' + $hist_is_success + ','+ $field + ',"' + $word +'")' : '#'}
                                   ${data.page >= data.maxPage ? "class='page-link disabled' style='color:gray'" : 'class="page-link"'}>다음</a> 
                               </li>
                         </ul>`;

                
                // 페이지네이션이 위치할 div 태그에 추가 
                $('#main > div.container > div > div > div > div:nth-child(2) > div:nth-child(3)').append(pagination);

            } else {
                // list.length 가 0 이면 출력되는 코드
                let empty = `<tr><td colspan="7">검색 결과가 존재하지 않습니다.</td></tr>`;

                $('#signHistTbody').empty(); // 테이블 비우기
                $('#signHistTbody').append(empty);
                $('#main > div.container > div > div > div > div:nth-child(2) > div:nth-child(3)').empty(); // 페이지네이션 비우기
            }
        }
    });
} // signHistoryListAjax end

// 페이지 로드 시
$(function() {

    signHistoryListAjax();
    // 파라미터 없이 페이지 호출.
    // 서블릿에서 기본으로 1 / 10 으로 출력

    // 페이지당 건 수 바꾸면 실행되는 이벤트 핸들러
    // 검색 데이터가 같이 넘어가게끔 해야 한다.
    $(document).on('change','#itemsPerPage', function() {
        const histTimeStart = $('#sighHistDate1').val().length == 0 ? undefined : $('#sighHistDate1').val();
        const histTimeEnd = $('#signHistDate2').val().length == 0 ? undefined : $('#signHistDate2').val();
        let hist_is_success = undefined;
        $('input[name=is_success]').each(function(){
            if($(this).prop('checked') == true) {
                hist_is_success = $(this).val();
            }
        });
        const signHist_searchField = $('#main > div.container > div > div > div > div:nth-child(1) > form > div:nth-child(3) > div.input-group.align-self-center > div.form-group.col-3.col-md-auto.mx-2').find('select[name="signHist_searchField"]').val();
        const signHist_searchWord = $('#search_word').val().trim().length == 0 ? undefined : $('#search_word').val().trim();

        signHistoryListAjax(1, $(this).val(), histTimeStart, histTimeEnd, hist_is_success, signHist_searchField,signHist_searchWord );

    });
    
    // 검색 폼의 submit 클릭시 기본 이벤트 막고 각 input 태그에서 값 가져오고 ajax 실행하는 이벤트 핸들러 함수
    $(document).on('click', '#main > div.container > div > div > div > div:nth-child(1) > form > div:nth-child(3) > div.input-group.align-self-center > div.form-group.col-auto.col-md-auto > button', function(e){
        e.preventDefault();
        let limit = $('#itemsPerPage').val(); // 건 수
        const histTimeStart = $('#sighHistDate1').val().length == 0 ? undefined : $('#sighHistDate1').val();
        const histTimeEnd = $('#signHistDate2').val().length == 0 ? undefined : $('#signHistDate2').val();
        console.log(histTimeStart); // 날짜 검색 시작 값 YYYY-MM-DD
        console.log(histTimeEnd); // 날짜 검색 끝 값 YYYY-MM-DD
        let hist_is_success = undefined;
        $('input[name=is_success]').each(function(){
            if($(this).prop('checked') == true) {
                hist_is_success = $(this).val();
            }
        });
        console.log(hist_is_success); // 로그인 성공 여부 전체 / 성공 / 실패

        const signHist_searchField = $('#main > div.container > div > div > div > div:nth-child(1) > form > div:nth-child(3) > div.input-group.align-self-center > div.form-group.col-3.col-md-auto.mx-2').find('select[name="signHist_searchField"]').val();
        console.log(signHist_searchField); // 검색 필드 전체 / 이메일  등등
        const signHist_searchWord = $('#search_word').val().trim().length == 0 ? undefined : $('#search_word').val().trim();
        console.log(signHist_searchWord); // 검색어

        signHistoryListAjax(1, limit, histTimeStart, histTimeEnd, hist_is_success, signHist_searchField,signHist_searchWord );
    })
});