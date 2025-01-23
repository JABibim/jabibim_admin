$(function () {
    function resizeImage() {
        const img = document.getElementById("careerImage");
        const containerWidth = img.parentElement.offsetWidth; // 부모 요소의 너비

        // 이미지의 최대 너비를 부모 컨테이너 너비로 설정
        if (img.naturalWidth > containerWidth) {
            img.style.width = `${containerWidth}px`;
            img.style.height = "auto"; // 비율 유지
        } else {
            img.style.width = `${img.naturalWidth}px`; // 원래 너비
            img.style.height = "auto";
        }
    }

    // 페이지 로드 시 및 브라우저 크기 변경 시 함수 호출
    window.addEventListener("load", resizeImage);
    window.addEventListener("resize", resizeImage);
})