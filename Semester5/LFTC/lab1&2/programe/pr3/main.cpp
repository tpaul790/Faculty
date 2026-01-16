#include <iostream>
using namespace std;
int main()
{
    int n;
    cin>>n;
    int s = 0;
    while(n != 0)
    {
        int a;
        cin>>a;
        s = s+a;
        n = n-1;
    }
    cout<<"suma="<<s;
    return 0;
}
