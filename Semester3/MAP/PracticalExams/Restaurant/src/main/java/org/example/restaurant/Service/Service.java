package org.example.restaurant.Service;

import org.example.restaurant.Domain.Entitati.MenuItem;
import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.Table;
import org.example.restaurant.Repository.MenuItemsRepository;
import org.example.restaurant.Repository.OrdersRepository;
import org.example.restaurant.Repository.TablesRepository;

import java.util.Set;

public class Service {
    private MenuItemsRepository menuItemsRepository;
    private OrdersRepository ordersRepository;
    private TablesRepository tablesRepository;

    public Service(MenuItemsRepository menuItemsRepository, OrdersRepository ordersRepository, TablesRepository tablesRepository) {
        this.menuItemsRepository = menuItemsRepository;
        this.ordersRepository = ordersRepository;
        this.tablesRepository = tablesRepository;
    }

    public Set<MenuItem> findAllMenuItems(){
        return menuItemsRepository.findAll();
    }

    public Set<Order> findAllOrders(){
        return ordersRepository.findAll();
    }

    public Set<Table> findAllTables(){
        return tablesRepository.findAll();
    }


}
