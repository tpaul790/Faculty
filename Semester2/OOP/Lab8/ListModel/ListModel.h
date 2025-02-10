//
// Created by Titieni Paul on 27.05.2024.
//

#ifndef LAB6_LISTMODEL_H
#define LAB6_LISTMODEL_H

#include "../Domain/produs.h"
#include <QAbstractListModel>
#include <utility>
#include <vector>
using std::vector;

class ListModel : public QAbstractListModel{
private:
//    Q_OBJECT
    vector<Produs> produse;
public:
    explicit ListModel(vector<Produs> prods): produse{std::move(prods)} {};

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    void setProduse(vector<Produs> prods);
};


#endif //LAB6_LISTMODEL_H
