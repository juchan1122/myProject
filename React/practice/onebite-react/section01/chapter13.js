//************************************************************************* */
// 1. 콜백 함수
// 정의: 자신이 아닌 다른 함수에, 인수로써 전달된 함수를 의미함
//************************************************************************* */
// 어떤 함수를 다른 함수의 인수로 전달해서 나중에 아라서 호출하도록 하는 함수(콜백 함수)
function main(value){
    value();
}

function sub(){
    console.log("sub");
}

main(sub); // main() 함수의 매개변수로 sub()함수가 들어감
console.log("/************************/");
//************************************************************************* */
function main01(value){
    value();
}

function sub01(){
    console.log("i am sub");
}

main01(sub01);
console.log("/************************/");
//************************************************************************* */
function main02(value){
    console.log(1);
    console.log(2);
    value();
    console.log("end!");
}

function sub02(){
    console.log("i am iorn man!!!");

}
main02(sub02);
console.log("/************************/");
//************************************************************************* */
main(function (){
    console.log("i am iron man!!!");
});
//************************************************************************* */
main(() => {
    console.log("i am iron man!!!");
});

console.log("/************************/");
//************************************************************************* */
// 2. 콜백함수의 활용
//************************************************************************* */
function repeat(count){
    for(let idx = 1; idx <= count; idx++){
        console.log(idx);
    }
}
repeat(5);
console.log("/************************/");
//************************************************************************* */
function repeatDouble(count){
    for(let idx = 1; idx <= count; idx++){
        console.log(idx * 2);
    }
}
repeatDouble(5);
console.log("/************************/");
//************************************************************************* */
function repeat01(count, callback){
    for(let idx = 1; idx <= count; idx++){
        callback(idx);
    }
}
repeat01(5, function (idx) {
    console.log(idx);
});
repeat01(5, function (idx) {
    console.log(idx * 2);
});
repeat01(5, function (idx) {
    console.log(idx * 3);
});
// 중복 코드를 제거하고 간결하게 코드를 작성이 가능
console.log("/************************/");
//************************************************************************* */
function repeat02(count, callback){
    for(let idx = 1; idx <= count; idx++){
        callback(idx);
    }
}
repeat01(5,  (idx) => {
    console.log(idx);
});
repeat01(5, (idx) => {
    console.log(idx * 2);
});
repeat01(5, (idx) => {
    console.log(idx * 3);
});
// 화살표로 변경