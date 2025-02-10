//
// Created by Titieni Paul on 22.06.2024.
//

#include "TableModel.h"

int TableModel::rowCount(const QModelIndex &parent) const{
    return produse.size();
}

int TableModel::columnCount(const QModelIndex &parent) const {
    return 4;
}

void TableModel::setProduse(vector<Produs> prods) {
    this->produse = std::move(prods);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), columnCount());
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}

QVariant TableModel::data(const QModelIndex &index, int role) const {
    if(role == Qt::DisplayRole){
        qDebug()<<"col: "<<index.column()<<' '<<"row: "<<index.row();
        auto col = index.column();
        auto produs = produse[index.row()];
        auto nume = produs.get_nume();
        auto prod = produs.get_producator();
        auto pret = produs.get_pret();
        auto tip = produs.get_tip();
        if(col == 0)
            return QString::fromStdString(nume);
        if(col == 1)
            return QString::fromStdString(tip);
        if(col == 2)
            return QString::fromStdString(prod);
        if(col == 3)
            return QString::number(pret);
    }
    return QVariant{};
}

QVariant TableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if(role == Qt::DisplayRole){
        if(orientation == Qt::Horizontal){
            if(section == 0)
                return QString("Nume");
            else if(section == 1)
                return QString("Tip");
            else if(section == 2)
                return QString("Producator");
            else if(section == 3)
                return QString("Pret");
        }
        else
            return QString("%1").arg(section+1);
    }
    return QVariant{};
}

