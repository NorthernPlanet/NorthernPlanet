const path = require('path');
module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/' : './', //deveopment.productions,test모드 중 배포시 루트로, 개발시 상대경로로 설정함.

  outputDir: path.resolve(
    __dirname,
    //'../backend/northernplanet/src/main/resources/static',
    // '../backend/webrtc/src/main/resources/static',
    //배포 설정
    './build',
  ), //빌드파일을 올릴 곳(디폴트(dist)에서 스프링 부트 static폴더로 )
  devServer: {
    historyApiFallback: true, // history mode 새로고침시 정상 작동
    port: 3000, //개발하는 동안 프런트엔드 페이지를 띄우는 포트
    https: true,
    proxy: {
      '/api/*': {
        target: 'https://localhost:8446', // /api 요청을 할때 백엔드 호출
      },
    },
  },
  css: {
    loaderOptions: {
      sass: {
        prependData: `
        @import "./src/assets/scss/global.scss";
        `,
      },
    },
  },
};
