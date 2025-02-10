//
// Created by Titieni Paul on 22.06.2024.
//

#ifndef LAB6_TABLEMODEL_H
#define LAB6_TABLEMODEL_H

#include <QAbstractTableModel>
#include <utility>
#include <vector>
#include "../Domain/produs.h"

using namespace std;

class TableModel : public QAbstractTableModel{
private:
//Q_OBJECT
    vector<Produs> produse;
public:
    explicit TableModel(vector<Produs> prods) : produse(std::move(prods)){}

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    QVariant headerData(int section, Qt::Orientation orientation,int role) const override;

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    void setProduse(vector<Produs> prods);

};


#endif //LAB6_TABLEMODEL_H
