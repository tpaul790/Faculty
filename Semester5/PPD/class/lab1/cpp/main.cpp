#include <iostream>
#include <thread>

using namespace std;

void worker() {
    this_thread::sleep_for(chrono::seconds(3));
    cout<<"hello world"<<endl;
}

int main() {
    thread t(worker);
    cout<<"hello world"<<endl;
    t.join();
    cout<<"joined thread"<<endl;
    return 0;
}
