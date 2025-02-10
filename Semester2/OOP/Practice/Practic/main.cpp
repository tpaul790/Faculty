#include <iostream>
#include <QApplication>
#include <QWidget>
int main(int argc, char* argv[]) {
    QApplication a{argc,argv};
    QWidget gui;
    gui.show();
    return QApplication::exec();
}
