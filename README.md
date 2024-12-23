## _Repo Clone_

```shell
$ git clone # 프로젝트 소스 불러오기
good~
$ git fetch # 프로젝트 최신화 

$ git checkout -b dev origin/dev # dev 브랜치 생성 및 이동

$ git branch # 브랜치 확인 ( dev, prod 두 개의 브랜치가 있어야 함 )
```

## _Github Rule_

- `prod` 브랜치의 소스는 배포용입니다.
- `dev` 브랜치의 소스는 개발용입니다.

1. `dev` 브랜치에서 개인별로 branch를 따온 후, 작업이 완료되면 `dev` 브랜치에 머지합니다.
2. `dev` 브랜치의 소스가 에러가 없다는 전제하에, `prod` 브랜치에 머지합니다. ( Github PR 활용 )

## _주의_

- `prod` 브랜치에는 절대로 직접적인 작업을 하지 않습니다.
- `dev` 브랜치에는 에러가 없는 소스만을 머지합니다.
- 세미 프로젝트때와 달리 배포도 진행되기 때문에 오류가 있는 소스를 머지하면 안됩니다.  
