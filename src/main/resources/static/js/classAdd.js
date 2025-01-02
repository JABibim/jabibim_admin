function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

function submitFormData() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: 'addClassProcess',
            type: 'POST',
            data: {
                course_id: $('#courseId').val(),
                class_subject: $('#class_subject').val(),
                class_content: $('#class_content').val(),
            },
            success: function (response) {
                if (response.class_id) {
                    resolve(response.class_id); // 서버로부터 class_id 반환받기
                } else {
                    reject(new Error("class_id를 반환받지 못했습니다."));
                }
            },
            error: function (error) {
                alert('기본 정보 저장 중 오류가 발생했습니다.');
                console.error(error);
                reject(error);
            }
        });
    });
}

function submitFormAndUploadFile() {
    submitFormData().then(classId => {
        console.log('====> classId : ', classId);

        const videoFile = $('#class_video')[0].files[0];
        const generalFile = $('#class_file')[0].files[0];

        const uploadPromises = []; // 프로미스객체 배열

        // 각각의 파일에 대해 업로드 프로세스 시작
        if (videoFile) {
            uploadPromises.push(uploadFile(videoFile, 'class_video', classId))
        }
        if (generalFile) {
            uploadPromises.push(uploadFile(generalFile, 'class_file', classId))
        }

        return Promise.all(uploadPromises);
    }).then(() => {
        console.log('강의가 성공적으로 등록되었습니다.');
        window.location.href = contextPath + '/content/class';
    }).catch(err => {
        console.log('파일 업로드중 오류가 발생했습니다.');
    })
}

function uploadFile(file, fileType, classId) {
    return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('fileType', fileType);
        formData.append('classId', classId); // class_id 추가

        $.ajax({
            url: 'addClassFilesProcess', // 파일 업로드 처리 엔드포인트
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            xhr: function () {
                const xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (evt) {
                    if (evt.lengthComputable) {
                        const percentComplete = Math.round((evt.loaded / evt.total) * 100);
                        updateProgress(percentComplete);
                    }
                }, false);
                return xhr;
            },
            success: function (response) {
                console.log(`${fileType} 업로드 완료`);
                updateProgress(100);
                resolve();
            },
            error: function (error) {
                updateProgress(0);
                reject(error);
            }
        });
    });
}

function updateProgress(percentage) {
    const progressBar = document.getElementById('progressBar');
    const progressText = document.getElementById('progressText');

    progressBar.style.width = percentage + '%'; // 프로그레스 바 너비 업데이트
    progressText.textContent = percentage + '%'; // 텍스트 업데이트

    if (percentage >= 100) {
        setTimeout(() => {
            document.getElementById('progressContainer').style.display = 'none'; // 완료 후 숨김
        }, 500);
    } else {
        document.getElementById('progressContainer').style.display = 'block'; // 프로그레스 바 표시
    }
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

    $('#clipImageVideo').click(function () {
        $('#class_video').click();
    })
    $('#clipImageFile').click(function () {
        $('#class_file').click();
    })
    $('#class_video').change(function () {
        const file = this.files[0];

        if (file) {
            const fileType = file.type;
            const validVideoTypes = ['video/mp4', 'video/webm', 'video/ogg'];

            if (!validVideoTypes.includes(fileType)) {
                showToast('영상 파일만 업로드 가능합니다. (허용 형식: mp4, webm, ogg)')

                $(this).val('');  // 입력값 초기화
                $('#videoFileValue').text(''); // 파일명 표시 영역 초기화
                return;
            }

            const inputFile = $(this).val().split('\\');
            $('#videoFileValue').text(inputFile[inputFile.length - 1]);
        }
    });
    $('#class_file').change(function () {
        const file = this.files[0];

        if (file) {
            // const fileType = file.type;
            const inputFile = $(this).val().split('\\');
            $('#fileValue').text(inputFile[inputFile.length - 1]);
        }
    });

    $('#submitBtn').on('click', function (e) {
        e.preventDefault(); // 기본 폼 제출 방지

        // 과정명
        const $courseId = $('#courseId');
        if (!$courseId.val()) {
            showToast('과정명을 선택해주세요.');
            $courseId.focus();

            return false;
        }

        // 강의명
        const $classSubject = $('#class_subject');
        if ($classSubject.val().trim() === '') {
            showToast('강의명을 입력해주세요.');
            $classSubject.focus();

            return false;
        }

        // 강의 정보
        const $classInfo = $('#class_info');
        if ($classInfo.val().trim() === '') {
            showToast('강의정보를 입력해주세요.');
            $classInfo.focus();

            return false;
        }

        // 상세 설명
        // quill에서 html태그가 포함된 데이터를 가져오는 법 : quill.root.innerHTML
        // html이 제거된 일반 문자열 데이터만 가져오는 법 : quill.getText()
        let introString = quill.getText();
        let introHtml = quill.root.innerHTML;
        if (introString.trim() === '') {
            showToast('강의를 표현하는 상세설명을 입력해주세요.');

            return false;
        }
        $('#class_content').val(introHtml);

        // 강의 동영상 또는 자료 체크
        const $classVideo = $('#class_video');
        const $classFile = $('#class_file');
        if ($classVideo[0].files.length <= 0 && $classFile[0].files.length <= 0) {
            showToast('강의 동영상 또는 파일 중 하나는 첨부해야합니다.');

            return false;
        }

        submitFormAndUploadFile();
    });
})