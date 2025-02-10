//
// Created by Titieni Paul on 24.06.2024.
//


#include "TableModel.h"

int TableModel::rowCount(const QModelIndex &parent) const{
    return produse.size();
}

int TableModel::columnCount(const QModelIndex &parent) const {
    return 5;
}

void TableModel::setProduse(vector<Produs> prods) {
    this->produse = std::move(prods);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}

void TableModel::setSlider(int s) {
    this->slider=s;
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}

QVariant TableModel::data(const QModelIndex &index, int role) const {
    auto col = index.column();
    auto produs = produse[index.row()];
    auto nume = produs.get_nume();
    auto tip = produs.get_tip();
    auto pret = produs.get_pret();
    auto id = produs.get_id();
    if(role == Qt::DisplayRole){
        qDebug()<<"col: "<<index.column()<<' '<<"row: "<<index.row();
        auto nrVoc = 0;
        for(const auto& e : nume){
            if (e == 'a' or e == 'e' or e == 'i' or e == 'o' or e == 'u' or e == 'A' or e == 'E' or e == 'I' or e == 'O' or e == 'U')
                nrVoc++;
        }
        if(col == 0)
            return QString::number(id);
        if(col == 1)
            return QString::fromStdString(nume);
        if(col == 2)
            return QString::fromStdString(tip);
        if(col == 3)
            return QString::number(pret);
        if(col == 4)
            return QString::number(nrVoc);
    }
    if(role == Qt::BackgroundRole){
        if(pret<=slider) {
            QBrush bg(Qt::red);
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
                return QString("Nume");
            else if(section == 2)
                return QString("Tip");
            else if(section == 3)
                return QString("Pret");
            else if(section == 4)
                return QString("NrVocale");
        }
        else
            return QString("%1").arg(section+1);
    }
    return QVariant{};
}

