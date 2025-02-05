package org.example.restaurant.Service;

import org.example.restaurant.Domain.Entitati.*;
import org.example.restaurant.Observer.Observable;
import org.example.restaurant.Observer.Observer;
import org.example.restaurant.Observer.OrderEvent;
import org.example.restaurant.Observer.OrderEventType;
import org.example.restaurant.Repository.MenuItemsRepository;
import org.example.restaurant.Repository.OrdersRepository;
import org.example.restaurant.Repository.TablesRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service implements Observable<OrderEvent> {
    private MenuItemsRepository menuItemsRepository;
    private OrdersRepository ordersRepository;
    private TablesRepository tablesRepository;
    private List<Observer<OrderEvent>> observers;

    public Service(MenuItemsRepository menuItemsRepository, OrdersRepository ordersRepository, TablesRepository tablesRepository) {
        this.menuItemsRepository = menuItemsRepository;
        this.ordersRepository = ordersRepository;
        this.tablesRepository = tablesRepository;
        this.observers = new ArrayList<>();
    }

    public void addOrder(int tableId, List<Integer> itemids){
        ordersRepository.save(tableId);
        //obtin id ul comentii si adaug toate itemele comandate
        int idOrder = findAllOrders().stream().sorted().toList().getLast().getId();
        ordersRepository.saveItems(idOrder, itemids);

        //arunc cate un event pentru fiecare produs comandat
        for(Integer itemid : itemids){
            String nume =  menuItemsRepository.findOne(itemid).getItem();
            OrderDto data = new OrderDto(tableId,nume, LocalDateTime.now());
            OrderEvent event = new OrderEvent(OrderEventType.PLACE,data);
            notify(event);
        }
    }

    public Set<OrderDto> findAllOrdersDto(){
        Set<OrderDto> orderDtos = new HashSet<>();
        Set<Tuple<Integer,Integer>> items = ordersRepository.findAllItems();
        for(Tuple<Integer,Integer> item : items){
            Order order = ordersRepository.findOne(item.getT1());
            MenuItem menuItem = menuItemsRepository.findOne(item.getT2());
            OrderDto orderDto = new OrderDto(order.getTable(),menuItem.getItem(),order.getOrderDate());
            orderDtos.add(orderDto);
        }
        return orderDtos.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
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

    public Map<String, List<MenuItem>> getMenuItemsByCategories(Set<MenuItem> items) {
        return items.stream().collect(Collectors.groupingBy(MenuItem::getCategory));
    }

    @Override
    public void addObserver(Observer<OrderEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<OrderEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(OrderEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
