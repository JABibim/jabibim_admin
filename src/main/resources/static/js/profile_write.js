function showToast(message) {
    $('#validationToast .toast-body').text(message);

    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    toast.show();
}

$(function () {
    $(document).on('change', '#career_image', function (event) {
        const fileInput = event.target;
        const previewImage = document.getElementById('career_image');

        if (fileInput.files && fileInput.files[0]) {
            const file = fileInput.files[0];
            if (!file.type.startsWith('image/')) {
                showToast('이미지 파일만 업로드할 수 있습니다.');
                fileInput.value = '';
                previewImage.style.display = 'none';
            }
        } else {
            previewImage.style.display = 'none';
        }
    });

    const dropZone = document.getElementById("dropZone");
    const fileInput = document.getElementById("career_image");
    const previewImage = document.getElementById("previewImage");
    const imgDeleteBtn = document.getElementById("imgDeleteBtn");

    dropZone.addEventListener("click", () => {
        fileInput.click();
    });

    fileInput.addEventListener("change", (event) => {
        const file = event.target.files[0];
        if (file) {
            displayPreview(file);
        }
    });

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

    function adjustImageSize(img) {
        img.style.maxWidth = "100%"; // 부모 요소 너비에 맞추기
        img.style.height = "auto";  // 비율 유지
        img.style.display = "block";
        img.style.margin = "0 auto"; // 가운데 정렬
    }

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