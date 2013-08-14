描述：网页词典内容截取器  
  
性能指标：本机测试离线查询单词200ms之内
用法：FetchWord [-m <outputType>][ -iw <word>|-is <file>][ -d <dir>|-of <file>][ -s <outputStyle> ][ -c <dictionary>][ -p <pronounceLocal>][ -e <executableMp3PlayerLocation>][ | -h]
  
  -iw     指定单词  
  -is     指定单词列表文件，程序扫描该文件读取所有单词并抓取其释义  
  -od     指定一个文件夹存放抓取结果  
  -of     指定一个文件存放抓取结果  
  -m      指定输出类型  
           默认是  
            print  
           可能的值有  
            print    将结果打印在屏幕上  
            folder   将结果存入指定的文件夹中  
                      参数列表中必须包含-d选项并指明存储的位置  
            file     将结果存入单个文件中  
                      参数列表中必须包含-f选项并指明存储文件的位置  
           通常情况下，不指定输出位置，程序将直接打印，否则输出结果到指定位置  
            只要输出类型不特殊注明为其它输出方式：  
             如果使用-of指定了输出文件，则输出结果到单独的文件  
             如果使用-od指定了输出文件夹，则输出结果到指定的文件夹  
  -c      指定词典  
           默认值是  
            youdaocollins  
           可能的值有  
            youdaocollins   从有道词典网站获取Collins词典的释义  
  -s      指定输出内容的格式  
           默认是  
            plain  
           可能的值有  
            plain           纯文本方式显示内容  
            singleStyle     将样式直接写入HTML中  
			separateStyle   将样式分布在HTML页面之外的css文件中  
  -p      读出单词的读音，如果未发声，说明没有相应的声音文件  
           默认值为  
            en  
           可能的值有  
            en              使用英国口音读出单词  
            us              使用美国口音读出单词  
            no              不要读出单词  
  -e      设置可执行发声工具的路径，系统将调用该工具读出单词  
           默认使用/usr/bin/mpg321  
           可能的值有  
            mpg321  
  
  -h      显示本帮助  
  
用法示例：  
  
1.查看帮助  
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -h  
  
2.获取 hello 的释义并将结果打印出来  
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -iw hello -m print  
  
3.获取 hello 的释义并将结果存入 "/home/mindfine/dict/" 文件夹中  
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -iw hello -od /home/mindfine/dict/  
  
4.获取 hello 的释义并将结果存入 "/home/mindfine/dict.txt" 文件中  
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -iw hello -of /home/mindfine/dict.txt  
  
5.解析 "/home/mindfine/words.txt" 中的所有单词，并将释义存入 "/home/mindfine/dict/" 文件夹中  
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -is /home/mindfine/words.txt -od /home/mindfine/dict/  
  
6.解析 "/home/mindfine/words.txt" 中的所有单词，并将释义写入 "/home/mindfine/dict.txt" 文件中  
java -cp .:/home/mindfine/Java/lib/jsoup/jsoup-1.7.2.jar com.mindfine.youdaodict.FetchWord -is /home/mindfine/words.txt -of /home/mindfine/dict.txt  
  
  
源单词文件的格式为：  
hello  
many  
tiny  
funny  
...  
  
请在项目主页报告程序的错误，欢迎任何形式的复制和转发  
项目主页：https://github.com/rankun203/YoudaoDict
