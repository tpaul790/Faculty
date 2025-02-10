
#include "TabelModel.h"

int TableModel::rowCount(const QModelIndex &parent) const{
    return melodii.size();
}

int TableModel::columnCount(const QModelIndex &parent) const {
    return 5;
}

void TableModel::setProduse(vector<Melodie> prods) {
    this->melodii = std::move(prods);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit layoutChanged();
    emit dataChanged(topLeft, bottomRight);
}

QVariant TableModel::data(const QModelIndex &index, int role) const {
    if(role == Qt::DisplayRole){
        qDebug()<<"col: "<<index.column()<<' '<<"row: "<<index.row();
        auto col = index.column();
        auto melodie = melodii[index.row()];
        auto titlu = melodie.get_titlu();
        auto artist = melodie.get_artist();
        auto id = melodie.get_id();
        auto rank = melodie.get_rank();
        if(col == 0)
            return QString::number(id);
        if(col == 1)
            return QString::fromStdString(titlu);
        if(col == 2)
            return QString::fromStdString(artist);
        if(col == 3)
            return QString::number(rank);
        if(col == 4)
            return QString::number(rank);
    }
    return QVariant{};
}

QVariant TableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if(role == Qt::DisplayRole){
        if(orientation == Qt::Horizontal){
            if(section == 0)
                return QString("Id");
            else if(section == 1)
                return QString("Titlu");
            else if(section == 2)
                return QString("Artist");
            else if(section == 3)
                return QString("Rank");
            else if(section == 4)
                return QString("RankNr");
        }
        else
            return QString("%1").arg(section+1);
    }
    return QVariant{};
}

