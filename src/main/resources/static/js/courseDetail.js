let selectedClassId = null;

function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

const deleteClass = (courseId, classId) => {
    $.ajax({
        url: 'deleteClass',
        data: {courseId, classId},
        dataType: 'json',
        cache: false,
        success: function (data) {
            selectedClassId = null;

            if (data.result === 1) {
                showToast('과정이 정상적으로 삭제되었습니다.');
            } else {
                showToast('과정 삭제도중 오류가 발생했습니다.');
            }

            $('#deleteClassModal').modal('hide');

            getCourseClassList(courseId);
        }, error: function () {
            console.log('과정에 속한 강의 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
}
const loadClassDetails = (classId, targetElement) => {
    $.ajax({
        url: 'getClassDetail',
        type: 'GET',
        data: {classId},
        dataType: 'json',
        success: function (data) {
            const {class_content} = data.classDetailInfo;
            const fileList = data.classFileDetailList;

            const html = `
                <div class="d-flex">
                    <div style="width: 50%; padding-right: 20px; border-right: 1px solid #ddd;">
                        <h5><strong>과정 설명</strong></h5>
                        <p>${class_content}</p>
                    </div>
                    
                    <div style="width: 50%; padding-left: 20px;">
                        <h5><strong>강의 자료</strong></h5>
                        ${fileList && fileList.length > 0 ? `
                            <div class="file-list">
                                ${fileList.map(file => `
                                    <div class="file-card" style="border: 1px solid #ddd; border-radius: 8px; padding: 15px; margin-bottom: 15px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);">
                                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                            <!-- 파일 이름 -->
                                            <h6 style="margin: 0; flex: 1;">${file.class_file_origin}</h6>
                                            <!-- 다운로드 버튼 -->
                                            <a href="downloadClassFile?filePath=${encodeURIComponent(file.class_file_path)}&fileOriginName=${encodeURIComponent(file.class_file_origin)}" 
                                               style="color: #007bff; text-decoration: none;" title="파일 다운로드" download>
                                                <i class="bi bi-download" style="font-size: 1.5rem;"></i>
                                            </a>
                                        </div>
                                        <p style="margin: 0;">
                                            <small><strong>파일 유형:</strong> ${file.class_file_type}</small><br>
                                            <small><strong>파일 크기:</strong> ${formatFileSize(file.class_file_size)}</small><br>
                                            <!-- <small><strong>경로:</strong> ${file.class_file_path}</small> TODO [chan] 경로가 필요 없을듯 해서 일단 주석처리합니다. -->
                                        </p>
                                    </div>
                                `).join('')}
                            </div>
                        ` : `<p>등록된 파일이 없습니다.</p>`}
                    </div>
                </div>
            `;

            $(targetElement).html(html);
        },
        error: function () {
            console.log(`Failed to load details for classId ${classId}`);
            $(targetElement).html('<p>세부 정보를 불러오는 데 실패했습니다.</p>');
        }
    });
};
const formatFileSize = (sizeInBytes) => {
    if (sizeInBytes < 1024) {
        return `${sizeInBytes} Bytes`; // 1KB 미만 파일
    } else if (sizeInBytes < 1024 * 1024) {
        return `${(sizeInBytes / 1024).toFixed(2)} KB`; // 1MB 미만 파일
    } else {
        return `${(sizeInBytes / 1024 / 1024).toFixed(2)} MB`; // 1MB 이상 파일
    }
};
const displayClassList = (data) => {
    const {classCount, classList} = data;

    if (!classList || classList.length === 0) {
        $('#classInfoContainer').html('<p>등록된 강의가 없습니다.</p>');
        return;
    }

    let html = `<p>총 <b>${classCount}</b>개의 강의가 등록되어 있습니다.</p>`;
    html += '<div class="accordion" id="accordionExample">';
    classList.forEach(item => {
        const {class_id, class_name, class_seq} = item;

        // 각 아코디언에 고유 id 설정
        const headerId = `heading${class_seq}`;
        const collapseId = `collapse${class_seq}`;

        html += `
            <div class="accordion-item">
                <h2 class="accordion-header" id="${headerId}" style="display: flex; align-items: center;">
                    <!-- 아코디언 버튼 -->
                    <button class="accordion-button collapsed" type="button" data-class-id="${class_id}" data-bs-toggle="collapse" data-bs-target="#${collapseId}" aria-expanded="false" aria-controls="${collapseId}">
                        #${class_seq}&nbsp;&nbsp;${class_name}
                    </button>
                    <!-- 삭제 버튼 -->
                    <button type="button" class="btn btn-danger btn-sm me-2 deleteClassBtn" data-class-id="${class_id}" title="강의 삭제">
                        <i class="bi bi-trash"></i>
                    </button>
                </h2>
                <div id="${collapseId}" class="accordion-collapse collapse" aria-labelledby="${headerId}" data-bs-parent="#accordionExample">
                    <div class="accordion-body" id="classBody${class_id}">
                        <p>로딩 중...</p>
                    </div>
                </div>
            </div>
        `;
    });
    html += '</div>';

    $('#classInfoContainer').html(html);
};
const getCourseClassList = (courseId) => {
    $.ajax({
        url: 'getCourseClassList',
        data: {
            courseId
        },
        dataType: 'json',
        cache: false,
        success: function (data) {
            displayClassList(data);
        }, error: function () {
            console.log('과정에 속한 강의 목록 Ajax 호출 도중 에러가 발생했습니다.');
        }
    })
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
    quill.root.innerHTML = courseIntroContent;

    $(`input[type="radio"][name="course_diff"][value="${courseDiff}"]`).prop("checked", true);

    $('form[name=courseForm]').submit(function () {
        // 과정명
        const $courseName = $('#course_name');
        if ($courseName.val().trim() === '') {
            showToast('과정명을 입력해주세요.');
            $courseName.focus();

            return false;
        }

        // 과목명
        const $courseSubject = $('#course_subject');
        if ($courseSubject.val().trim() === '') {
            showToast('과정이 속한 과목(예) 수학, 과학, ...)을 입력해주세요.');
            $courseSubject.focus();

            return false;
        }

        // 상세 설명
        // quill에서 html태그가 포함된 데이터를 가져오는 법 : quill.root.innerHTML
        // html이 제거된 일반 문자열 데이터만 가져오는 법 : quill.getText()
        let introString = quill.getText();
        let introHtml = quill.root.innerHTML;
        if (introString.trim() === '') {
            showToast('과정을 표현하는 상세설명을 입력해주세요.');

            return false;
        }
        $('#course_intro').val(introHtml);

        // 수강 금액
        const $coursePrice = $('#course_price');
        if ($coursePrice.val().trim() === '') {
            showToast('수강금액 입력해주세요.');

            return false;
        }

        return true;
    })

    $('#modifyBtn').click(function () {
        $('#course_intro').val(quill.root.innerHTML);
    });

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

                $profileImage.hide();
            }
        }
    });

    $('#course_price').on('input', function () {
        $(this).val($(this).val().replace(/[^0-9]/g, ''));
    });

    $(document).on('click', '#toggleButton', function () {
        $('.card').addClass('shrink'); // 카드 너비 줄이기
        $('#newContent').fadeIn();    // 새로운 콘텐츠 표시

        getCourseClassList(courseId);
    });

    $(document).on('click', '#closeButton', function () {
        $('.card').removeClass('shrink'); // 카드 너비 원래대로
        $('#newContent').fadeOut();       // 새로운 콘텐츠 숨기기
    });

    $(document).on('click', '.accordion-button', function () {
        const classId = $(this).data('class-id');
        const targetBody = `#classBody${classId}`;

        if ($(targetBody).text().trim() === '로딩 중...') {
            loadClassDetails(classId, targetBody);
        } else {
            console.log('파일의 상세설명이 있습니다.');
        }
    });

    $(document).on('click', '.deleteClassBtn', function () {
        selectedClassId = $(this).data('class-id');
        $('#deleteClassModal').modal('show');
    });

    $(document).on('click', '.confirmDeleteBtn', function () {
        deleteClass(courseId, selectedClassId);
    })

    const dropZone = document.getElementById("dropZone");
    const fileInput = document.getElementById("update_course_image");
    const previewImage = document.getElementById("updatePreviewImage");
    const imgReSelectBtn = document.getElementById("imgReSelectBtn");

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
            imgReSelectBtn.style.display = 'inline-block';
        };
        reader.readAsDataURL(file);
    }

    imgReSelectBtn.addEventListener("click", (event) => {
        event.preventDefault();

        previewImage.src = "";
        previewImage.style.display = "none";

        fileInput.value = "";
        imgReSelectBtn.style.display = "none";
    });

    $(document).on('click', '#confirmUpdateBtn', () => {
        // 파일 입력 요소와 미리보기 이미지 참조
        const fileInput = document.getElementById("update_course_image");
        const previewImage = document.getElementById("previewImage");

        // 파일이 선택되었는지 확인
        if (fileInput.files.length === 0) {
            showToast('파일이 선택되지 않았습니다. 이미지를 선택하거나 업로드해주세요.');
            return;
        }

        // 선택된 파일 가져오기
        const file = fileInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                // 기존 이미지 창에 선택한 이미지 표시
                previewImage.src = e.target.result;
                previewImage.style.display = "block";
            };
            reader.readAsDataURL(file);

            // 기존 파일 input 태그에 파일 값 할당
            const mainFileInput = document.getElementById("course_image");
            mainFileInput.files = fileInput.files;

            $('#is_profile_changed').val(true);

            // 모달 닫기
            $('#verticalycentered').modal('hide');
        } else {
            showToast('유효한 이미지 파일을 선택해주세요.');
        }
    })

    $('#deleteCourseModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const courseId = button.data('course-id');

        $(this).find('#confirmCourseDeleteBtn').data('course-id', courseId);
    });

    $(document).on('click', '#confirmCourseDeleteBtn', function () {
        const courseId = $(this).data('course-id');
        if (!courseId) {
            alert('삭제할 Course ID가 없습니다.');
            return;
        }

        $.ajax({
            url: '/content/deleteCourse',
            method: 'POST',
            data: {courseId},
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function () {
                $('#deleteCourseModal').modal('hide');

                location.href = '/content';
            },
            error: function () {
                alert('삭제 처리 중 문제가 발생했습니다.');
            }
        });
    });
})