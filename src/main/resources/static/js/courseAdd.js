function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

$(function () {
    const quill = new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{'header': '1'}, {'header': '2'}, {'font': []}],
                [{'list': 'ordered'}, {'list': 'bullet'}],
                ['bold', 'italic', 'underline'],
                ['link'],
                [{'align': []}],
            ]
        }
    });

    $('#submitBtn').click(function () {
        if (isAdmin) {
            showToast('강사 계정으로 접속한 뒤 진행해주세요.');
            return false;
        }

        // 과정명
        const $courseName = $('#course_name');
        const courseName = $courseName.val().trim();
        if (courseName === '') {
            showToast('과정명을 입력해주세요.');
            $courseName.focus();

            return false;
        }

        // 과목명
        const $courseSubject = $('#course_subject');
        const courseSubject = $courseSubject.val().trim();
        if (courseSubject === '') {
            showToast('과정이 속한 과목(예) 수학, 과학, ...)을 입력해주세요.');
            $courseSubject.focus();

            return false;
        }

        // 과정 대표 이미지
        const $courseImage = $('#course_image');
        if ($courseImage[0].files.length <= 0) {
            showToast('과정 대표 이미지를 첨부해주세요.');

            return false;
        }

        // 과정 상세 설명
        // quill에서 html태그가 포함된 데이터를 가져오는 법 : quill.root.innerHTML
        // html이 제거된 일반 문자열 데이터만 가져오는 법 : quill.getText()
        let introString = quill.getText();
        let introHtml = quill.root.innerHTML;
        if (introString.trim() === '') {
            showToast('과정을 표현하는 상세 설명을 입력해주세요.');

            return false;
        }
        $('#course_intro').val(introHtml);

        // 과정 수강금액
        const $coursePrice = $('#course_price');
        if ($coursePrice.val().trim() === '') {
            showToast('수강금액 입력해주세요.');

            return false;
        }

        // 과정 태그
        const courseTag = $('#course_tag').val();

        console.log('============> 🚨🚨 등록 버튼 클릭!! 🚨🚨');

        console.log('과정명:', courseName);
        console.log('과목명:', courseSubject);
        console.log('과정 대표 이미지:', $courseImage[0].files[0]);
        console.log('상세 설명:', introHtml);
        console.log('수강 금액:', +$coursePrice.val());
        console.log('태그: ', courseTag);
        console.log('난이도:',);

        const courseIntro = introHtml;
        const coursePrice = +$coursePrice.val();
        const courseDiff = $('input[name="course_diff"]:checked').val();

        const formData = new FormData();
        const courseInfo = {
            courseName,
            courseSubject,
            courseIntro,
            coursePrice,
            courseDiff,
            courseTag
        };
        formData.append("courseData", new Blob(
            [JSON.stringify(courseInfo)],
            {type: "application/json"}));
        formData.append("courseImage", $courseImage[0].files[0]);

        fetch("addCourseProcess", {
            method: "POST",
            headers: {
                [header]: token,
            },
            body: formData,
        }).then((res) => {
            console.log('==> res : ', res);
        }).catch((err) => {
            console.log('==> err : ', err);
        })
    })

    $('#clipImage').click(function () {
        $('#course_image').click();
    })

    $('#course_image').change(function () {
        const file = this.files[0];
        const $profileImage = $('#previewImage');

        if (file) {
            const fileType = file.type;

            if (fileType.startsWith('image/')) {
                const inputFile = $(this).val().split('\\');
                $('#fileValue').text(inputFile[inputFile.length - 1]);

                const reader = new FileReader();
                reader.onload = function (e) {
                    $profileImage.attr('src', e.target.result);
                    $profileImage.show();
                }

                reader.readAsDataURL(file);
            } else {
                showToast('이미지 파일만 업로드 가능합니다.');

                $(this).val('');
                $('#fileValue').text('');
                $profileImage.hide();
            }
        }
    });

    $('#course_price').on('input', function () {
        $(this).val($(this).val().replace(/[^0-9]/g, ''));
    });

    const dropZone = document.getElementById("dropZone");
    const fileInput = document.getElementById("course_image");
    const previewImage = document.getElementById("previewImage");
    const imgDeleteBtn = document.getElementById("imgDeleteBtn");

    // 클릭 시 파일 선택 창 열기
    dropZone.addEventListener("click", () => {
        fileInput.click();
    });

    // 파일 선택 시 미리보기 처리
    fileInput.addEventListener("change", (event) => {
        const file = event.target.files[0];
        if (file) {
            displayPreview(file);
        }
    });

    // 드래그앤드롭 이벤트 처리
    dropZone.addEventListener("dragover", (event) => {
        event.preventDefault();
        dropZone.classList.add("dragover");
    });

    dropZone.addEventListener("dragleave", () => {
        dropZone.classList.remove("dragover");
    });

    dropZone.addEventListener("drop", (event) => {
        event.preventDefault();
        dropZone.classList.remove("dragover");

        const file = event.dataTransfer.files[0];
        if (file) {
            fileInput.files = event.dataTransfer.files; // 파일 입력에 할당
            displayPreview(file);
        }
    });

    function displayPreview(file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            previewImage.src = e.target.result;
            previewImage.style.display = "block";
            adjustImageSize(previewImage);

            imgDeleteBtn.style.display = 'inline-block';
        };
        reader.readAsDataURL(file);
    }

    // 이미지 크기 조정 (화면 비율에 맞추기)
    function adjustImageSize(img) {
        img.style.maxWidth = "100%"; // 부모 요소 너비에 맞추기
        img.style.height = "auto";  // 비율 유지
        img.style.display = "block";
        img.style.margin = "0 auto"; // 가운데 정렬
    }

    // 이미지 삭제 버튼 클릭 이벤트
    imgDeleteBtn.addEventListener("click", (event) => {
        event.preventDefault();

        // 미리보기 이미지 숨기기
        previewImage.src = "";
        previewImage.style.display = "none";

        // 파일 입력 초기화
        fileInput.value = "";

        // 삭제 버튼 숨기기
        imgDeleteBtn.style.display = "none";
    });
})