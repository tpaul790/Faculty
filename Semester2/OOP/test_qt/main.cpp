#include <QApplication>
#include <qlabel.h>
#include <qpushbutton.h>
#include <qboxlayout.h>
#include <qlineedit.h>
#include <qformlayout.h>


int main(int argc, char** argv) {
    QApplication a(argc, argv);
    QWidget w{};

    auto form=new QFormLayout;
    form->addRow("Nume",new QLineEdit);
    form->addRow("Tip",new QLineEdit);
    form->addRow("Pret",new QLineEdit);
    form->addRow("Producator",new QLineEdit);

    auto ly_buttons=new QVBoxLayout{};
    ly_buttons->addLayout(form);
    w.setLayout(ly_buttons);
    ly_buttons->addWidget(new QPushButton("Adauga"));
    ly_buttons->addWidget(new QPushButton("Sterge"));
    ly_buttons->addWidget(new QPushButton("Modifica"));
    w.show();
    return QApplication::exec();
}
