! Lecture7/mpi/tmp.f90

program tmp

    use mpi
    implicit none
    integer :: i, j, ierr, numprocs, proc_num
    integer::status(MPI_STATUS_SIZE)

    call mpi_init(ierr)
    call mpi_comm_size(MPI_COMM_WORLD, numprocs, ierr)
    call mpi_comm_rank(MPI_COMM_WORLD, proc_num, ierr)

    if (proc_num == 4) then 
        i = 55 
        call MPI_SEND(i, 1, MPI_INTEGER, 3, 21,  MPI_COMM_WORLD, ierr)
    endif 
    
    if (proc_num == 3) then 
        call MPI_RECV(j, 1, MPI_INTEGER, 4, 21,  MPI_COMM_WORLD, status, ierr)
        print *, "j = ", j 
    endif

    call mpi_finalize(ierr)

end program tmp