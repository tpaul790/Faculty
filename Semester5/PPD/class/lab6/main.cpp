#include <iostream>
#include <string>
#include <mpi.h>
#include <cassert>

using namespace std;

void mpi_hello(int rank, int size){
    const char format[] = "Hello from: %10d\n";
    const int STRING_SIZE = sizeof(format) - 1 + 10 - 4;
    if(rank == 0){
        //main
        char* buff = new char[STRING_SIZE  * (size - 1) + 1];
        buff[STRING_SIZE  * (size - 1) + 1] = '\0';
        auto* reqs = new MPI_Request[size - 1];

        for(int pid = 1; pid < size; pid++){
            MPI_Irecv(buff + STRING_SIZE * (pid - 1), STRING_SIZE, MPI_CHAR, pid, 0, MPI_COMM_WORLD, &reqs[pid-1]);
        }

//        bool ok = false;
//        while(!ok) {
//            ok = true;
//            for (int pid = 1; pid < size; pid++) {
//                int flag;
//                MPI_Test(&reqs[pid - 1], &flag, MPI_STATUSES_IGNORE);
//
//                if(flag == 0){
//                    ok = false;
//                }else{
//                    printf("%d arrived\n", pid);
//                }
//            }
//        }

//        MPI_Waitall(size - 1, reqs, MPI_STATUSES_IGNORE);

        for(int i = 0; i < size - 1; i++){
            int idx;
            MPI_Waitany(size - 1, reqs, &idx, MPI_STATUSES_IGNORE);

            printf("%s\n", buff);
        }

        delete[] buff;
        delete[] reqs;
    }else{
        //worker
        MPI_Request req;
        char* buff = new char[STRING_SIZE + 1];

        snprintf(buff, STRING_SIZE + 1, format, rank);

        MPI_Isend(buff, STRING_SIZE, MPI_CHAR, 0, 0, MPI_COMM_WORLD, &req);

        MPI_Wait(&req, MPI_STATUSES_IGNORE);
        delete[] buff;
    }
}

void init(int * vec, int len){
    for(int i = 0; i < len; i++)
        vec[i] = rand() % 270;
}

void validate(int* a, int* b, int* c, int len){
    for(int i = 0; i < len; i++){
        assert(c[i] == a[i] + b[i]);
    }
}

void mpi_sum(int rank, int size){
    const int N = 1000;
    if(rank == 0){
        int* a = new int[N];
        int* b = new int[N];
        int* c = new int[N];

        init(a, N);
        init(b, N);

        int slice = N / (size - 1);
        int r = N % (size - 1);
        int start = 0;
        int end = slice;

        auto* reqs = new MPI_Request[size - 1];

        for(int pid = 1; pid < size; pid++){
            if(r > 0){
                end++;
                r--;
            }

            int len = end - start;

            MPI_Send(&len, 1, MPI_INT, pid, 0, MPI_COMM_WORLD);
            MPI_Send(a + start, len, MPI_INT, pid, 0, MPI_COMM_WORLD);
            MPI_Send(b + start, len, MPI_INT, pid, 0, MPI_COMM_WORLD);

            //async
            MPI_Irecv(c + start, len, MPI_INT, pid, 0, MPI_COMM_WORLD, &reqs[pid - 1]);

            start = end;
            end += slice;
        }

        //async
        MPI_Waitall(size - 1, reqs, MPI_STATUSES_IGNORE);

        //sync
//        slice = N / (size - 1);
//        r = N % (size - 1);
//        start = 0;
//        end = slice;
//
//        for(int pid = 1; pid < size; pid++) {
//            if (r > 0) {
//                end++;
//                r--;
//            }
//
//            MPI_Recv(c + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);
//
//            start = end;
//            end += slice;
//        }

        delete[] a;
        delete[] b;
        delete[] c;
        delete[] reqs;
    }else{
        int len;
        MPI_Recv(&len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);

        int* a = new int[N];
        int* b = new int[N];
        int* c = new int[N];

        MPI_Recv(a, len, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);
        MPI_Recv(b, len, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);

        for(int i = 0; i < len; i++){
            c[i] = a[i] + b[i];
        }

        MPI_Send(c, len, MPI_INT, 0, 0, MPI_COMM_WORLD);

        delete[] a;
        delete[] b;
        delete[] c;
    }
}

void mpi_sum_scatter(int rank, int size) {
    int* a = nullptr;
    int* b = nullptr;
    int* c = nullptr;
    const int N = 1000;

    if(rank == 0){
        a = new int[N];
        b = new int[N];
        c = new int[N];

        init(a, N);
        init(b, N);

    }

    //calcule
    int dim = N / size;
    int* aloc = new int[dim];
    int* bloc = new int[dim];
    int* cloc = new int[dim];

    MPI_Scatter(a, dim, MPI_INT, aloc, dim, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, dim, MPI_INT, bloc, dim, MPI_INT, 0, MPI_COMM_WORLD);

    for(int i = 0; i < dim; i++){
        cloc[i] = aloc[i] + bloc[i];
    }

    MPI_Gather(cloc, dim, MPI_INT, c, dim, MPI_INT, 0, MPI_COMM_WORLD);

    if(rank == 0){
        validate(a, b, c, dim * size);
    }

    delete[] aloc;
    delete[] bloc;
    delete[] cloc;

    if(rank == 0){
        delete[] a;
        delete[] b;
        delete[] c;
    }
}

int main(int argc, char* argv[]) {
    printf("Hello\n");

    MPI_Init(&argc, &argv);

    MPI_Barrier(MPI_COMM_WORLD);

    int rank, size;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    printf("Hello from %d of %D\n", rank, size);

    int selected = stoi(argv[1]);

    switch (selected) {
        case 1:
            mpi_hello(rank, size);
            break;
        case 2:
            mpi_sum(rank, size);
            break;
        case 3:
            mpi_sum_scatter(rank, size);
            break;
        default:
            printf("Option unknow: %d\n", selected);
            break;
    }

    MPI_Finalize();
    return 0;
}
