# Logstash Korean Number Filter [![Build Status](https://app.travis-ci.com/twosom/logstash-filter-korean-number.svg?branch=main)](https://app.travis-ci.com/twosom/logstash-filter-korean-number)

이 플러그인은 [Logstash](https://github.com/elastic/logstash) 를 위한 필터 플러그인으로

Apache [Lucene](https://github.com/apache/lucene) 프로젝트의 Nori 분석기를 기반으로 작성되었습니다.

## Documentation

이 플러그인은 한국어 숫자처리를 위한 Java기반의 logstash 필터입니다.

사만삼천오백, 4만3천5백, 4.3천 등 한글로 표현된 숫자들을 손쉽게 아라비아 숫자로 변환할 수 있습니다.

## 사용방법

이 플러그인을 직접 빌드하기 위해서는 맨 처음에 `logstash-core` 라이브러리가 필요합니다.

1

```shell
git clone https://github.com/elastic/logstash.git
```

2

``` shell
cd ./logstash
```

3

``` shell
./gradlew clean assemble
```

4

``` shell
export LOGSTASH_CORE_PATH=$PWD/logstash-core
```

5

``` shell
cd ../
```

6

``` shell
git clone https://github.com/twosom/logstash-filter-korean-number.git
```

7

``` shell
echo "LOGSTASH_CORE_PATH=$LOGSTASH_CORE_PATH" >> gradle.properties
```

8

``` shell
./gradlew clean gem
```

9

``` shell
export KOREAN_NUMBER_PATH=$PWD/logstash-filter-korean_jamo-현재 자모필터 플러그인 버전.gem  
```

로그스태시가 설치 된 폴더로 이동 후

``` shell
/bin/logstash-plugin install $KOREAN_NUMBER_PATH 
```

### 2. 필터 설정

Add the following inside the filter section of your logstash configuration:

```sh

filter {
  
  korean_number {
    field => [            # 숫자로 변환하고자 하는 필드 이름들을 리스트로 설정합니다.
      "field1",
      "field2",
      ...
    ]
  }
                          # "삼만8천팔백원" 처럼, 한글로 표현 된 숫자가 있는 필드를 지정할 경우,
                          # 해당 필드에 "39800" 이라고 문자열로 재설정됩니다.
}
```

### 3. 예제

#### input

``` bash
bin/logstash -e "input { generator {'message' => '삼만9천팔백원'} } filter { korean_number { field => ['message'] }}  output { stdout{} }"
```

#### output

```shell
{
    "@timestamp" => 2022-03-24T14:30:49.984753Z,
      "@version" => "1",
         "event" => {
        "original" => "삼만9천팔백원",
        "sequence" => 17375
    },
          "host" => {
        "name" => "hopeui-MacBookPro.local"
    },
       "message" => "39800"
}
```