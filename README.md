描述：网页词典内容截取器
用法：FetchWord -w|-s -d
  -w     指定单词
  -d     指定路径存放抓取结果
  -s     指定单词列表文件，程序扫描该文件读取所有单词并抓取其释义
  -m     指定输出类型
          默认是
           print
          可能的值有
           print    将结果打印在屏幕上
           folder   将结果存入指定的文件夹中，参数列表中必须包含-d选项并指明存储的位置
           file     将结果存入单个文件中
  -h     显示本帮助

用法示例：

1.查看帮助
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -h

2.获取hello的释义并将结果打印出来
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -w hello -d /home/mindfine/dict

请在项目主页报告程序的错误，欢迎任何形式的复制和转发
项目主页：https://github.com/rankun203/YoudaoDict