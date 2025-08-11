echo "Current dir is " $(pwd)
cp -r src build
find build -name "*.java" > build/tmp.txt
javac -cp "$ANDROID_HOME/platforms/$(ls -r $ANDROID_HOME/platforms | head -n 1)/android.jar" -d ConsoleCanvasBuild @build/tmp.txt
jar -c -f ConsoleCanvas.jar -C ConsoleCanvasBuild .
rm -rf build
rm -rf ConsoleCanvasBuild
echo $(realpath ConsoleCanvas.jar)
