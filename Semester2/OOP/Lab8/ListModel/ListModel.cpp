//
// Created by Titieni Paul on 27.05.2024.
//

#include "ListModel.h"

#include <utility>

int ListModel::rowCount(const QModelIndex &parent) const {
    return produse.size();
}

QVariant ListModel::data(const QModelIndex &index, int role) const {
    if (role == Qt::DisplayRole) {
        auto prod = produse[index.row()];
        return QString::fromStdString(prod.get_nume() +" | "+prod.get_tip()+" | "+std::to_string(prod.get_pret())+" | "+prod.get_producator()+" ");
    }
    return QVariant{};
}

void ListModel::setProduse(vector<Produs> prods) {
    this->produse = std::move(prods);
    const QModelIndex& topLeft = createIndex(0, 0);
    const QModelIndex& bottomRight = createIndex(rowCount(), 0);
    emit dataChanged(topLeft, bottomRight);
}