#include "TableModel.h"

int TableModel::rowCount(const QModelIndex &parent) const{
    return parcari.size();
}

int TableModel::columnCount(const QModelIndex &parent) const {
    return 4;
}

void TableModel::setParcari(vector<Parcare> prs) {
    this->parcari = std::move(prs);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}



QVariant TableModel::data(const QModelIndex &index, int role) const {
    auto col = index.column();
    auto p = parcari[index.row()];
    string ad = p.get_adresa();
    string st = p.get_stare();
    int nrl = p.get_linii();
    int nrc = p.get_coloane();
    if(role == Qt::DisplayRole){
        //qDebug()<<"col: "<<index.column()<<' '<<"row: "<<index.row();

        if(col == 0)
            return QString::fromStdString(ad);
        if(col == 1)
            return QString::number(nrl);
        if(col == 2)
            return QString::number(nrc);
        if(col == 3)
            return QString::fromStdString(st);
    }
    return QVariant{};
}

QVariant TableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if(role == Qt::DisplayRole){
        if(orientation == Qt::Horizontal){
            if(section == 0)
                return QString("Adresa");
            else if(section == 1)
                return QString("NumarLinii");
            else if(section == 2)
                return QString("NumarColoane");
            else if(section == 3)
                return QString("Stare");
        }
        else
            return QString("%1").arg(section+1);
    }
    return QVariant{};
}