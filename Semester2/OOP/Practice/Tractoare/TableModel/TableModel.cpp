//
// Created by Titieni Paul on 26.06.2024.
//

#include "TableModel.h"

int TableModel::rowCount(const QModelIndex &parent) const{
    return tractoare.size();
}

int TableModel::columnCount(const QModelIndex &parent) const {
    return 5;
}

void TableModel::setTractoare(vector<Tractor> trs) {
    this->tractoare = std::move(trs);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}

void TableModel::setTip(string t) {
    this->tip = std::move(t);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}


QVariant TableModel::data(const QModelIndex &index, int role) const {
    auto col = index.column();
    auto t = tractoare[index.row()];
    auto id = t.get_id();
    auto nr = t.get_nrRoti();
    auto tip = t.get_tip();
    auto denumire = t.get_denumire();
    int nrTip = 0;
    for(const auto& tr : tractoare)
        if(tr.get_tip()==tip)
            nrTip++;
    if(role == Qt::DisplayRole){
        //qDebug()<<"col: "<<index.column()<<' '<<"row: "<<index.row();

        if(col == 0)
            return QString::number(id);
        if(col == 1)
            return QString::fromStdString(denumire);
        if(col == 2)
            return QString::fromStdString(tip);
        if(col == 3)
            return QString::number(nr);
        if(col == 4)
            return QString::number(nrTip);
    }
    else if(role == Qt::BackgroundRole){
        if(tip == this->tip) {
            QBrush br{Qt::red};
            return br;
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
                return QString("Tip");
            else if(section == 3)
                return QString("NrRoti");
            else if(section == 4)
                return QString("NrTip");
        }
        else
            return QString("%1").arg(section+1);
    }
    return QVariant{};
}