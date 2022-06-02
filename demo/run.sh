# Path to the JavaFX lib folder
JAVAFX_LIB_DIR=$PWD/lib/linux/lib/

echo $JAVAFX_LIB_DIR

java -jar --module-path $JAVAFX_LIB_DIR --add-modules=javafx.swing,javafx.graphics,javafx.fxml,javafx.media,javafx.web --add-reads javafx.graphics=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.charts=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.iio=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.iio.common=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.css=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED MathHelper.jar

echo LOADING...
