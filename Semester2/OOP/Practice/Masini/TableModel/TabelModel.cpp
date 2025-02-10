

#include "TabelModel.h"

int TableModel::rowCount(const QModelIndex &parent) const{
    return masini.size();
}

int TableModel::columnCount(const QModelIndex &parent) const {
    return 4;
}

void TableModel::setMasini(vector<Masina> cars) {
    this->masini = std::move(cars);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}

QVariant TableModel::data(const QModelIndex &index, int role) const {
    auto col = index.column();
    auto m = masini[index.row()];
    string id = m.get_id();
    string tip = m.get_tip();
    string denumire = m.get_denumire();
    int an = m.get_an();
    if(role == Qt::DisplayRole){
        //qDebug()<<"col: "<<index.column()<<' '<<"row: "<<index.row();
        if(col == 0)
            return QString::fromStdString(id);
        if(col == 1)
            return QString::fromStdString(denumire);
        if(col == 2)
            return QString::number(an);
        if(col == 3)
            return QString::fromStdString(tip);
    }
    if(role == Qt::BackgroundRole){
        if(tip=="suv") {
            QBrush bg(Qt::red);
            return bg;
        }else if (tip=="van"){
            QBrush bg(Qt::green);
            return bg;
        }else if (tip=="hatch"){
            QBrush bg(Qt::yellow);
            return bg;
        }else if (tip=="sedan"){
            QBrush bg(Qt::blue);
            return bg;
        }
    }
    return QVariant{};
}

QVariant TableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if(role == Qt::DisplayRole){
        if(orientation == Qt::Horizontal){
            if(section == 0)
                return QString("Id");
            else if(section == 1)
                return QString("Denumire");
            else if(section == 2)
                return QString("An");
            else if(section == 3)
                return QString("Tip");
        }
        else
            return QString("%1").arg(section+1);
    }
    return QVariant{};
}

