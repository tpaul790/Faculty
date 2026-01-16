#include <iostream>
using namespace std;
int main()
{
    int a;
    int b;
    cin>>a;
    cin>>b;
    while(b != 0)
    {
        int r = a%b;
        a = b;
        b = r;
    }
    cout<<"cmmdc="<<a;
    return 0;
}
