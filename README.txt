📁 Tess4J 4.5.5 OCR 运行依赖包

📦 包含内容：
- Tess4J 主程序库和所有依赖 .jar（放入项目 lib/ 并添加进 classpath）
- Windows 平台用的 DLL 动态库文件（放在项目根目录或系统 PATH）

✅ 使用方法：
1. 解压 zip 到你的 JavaOCR 项目目录
2. 保留 lib/ 结构，右键 NetBeans 项目 > Properties > Libraries > Classpath > Add JAR/Folder
3. 添加 lib/ 下所有 jar 文件
4. 保证 libtesseract-5.dll 和 liblept-5.dll 与项目在同一层目录
