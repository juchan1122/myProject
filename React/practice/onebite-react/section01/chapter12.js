//************************************************************************* */
// 1. 함수 표현식
//************************************************************************* */
// 함수 선언문으로 함수를 표현X
// 값으로서 함수를 생성 -> 함수 표현식 

function funcA(){
    console.log("funcA");
}

// 함수를 변수 이름에 담게 되면 변수이름으로 함수를 호출 가능
let varA = funcA;
varA();
//console.log(varA);

// 자바스크립트 -> 함수를 굳이 선언하지 않고 변수에 담듯이 할 수 있음

let varB = function funcB(){
    console.log("funcB");
}

let varB1 = function (){ // 익명 함수: 함수명 생략 가능
    console.log("funcB");
}

varB(); // 변수 이름으로 호출 - 정상 작동
//funcB(); 로 호출 시 오류남


// 향후 콜백함수에서 유용하게 활용



//************************************************************************* */
// 2. 화살표 함수
//************************************************************************* */
//  이전보다 빠르고 간결하게 표현이 목적
// 기존 함수
let varC1 = function () {
    return 1;
}
console.log('varC1 '+ varC1());

//  화살표 함수 적용
let varC2 = () => {
    return 1;
};
console.log('varC2 '+ varC2());

//  더 간결하게
let varC3 = () => 1; // return 생략, 1 반환
console.log('varC3 ' + varC3());

// 매개변수
let varC4 = (value) => value + 1;
console.log('varC4 ' + varC4(10))

// 매개변수 + 함수 로직 처리
let varC5 = (value) => {
    console.log('varC5-1 ' + value);
    return value + 1;
};
console.log('varC5-2 ' + varC5(10))