! Lecture4/looporder.f90

program looporder

    implicit none
    integer, parameter :: m = 4096, n = 10000 
    real(kind=8), dimension(m,n) :: a
    real(kind=8) :: elapsed_time
    integer(kind=8) :: tclock1, tclock2, tclock3, clock_rate
    integer :: i,j

    call system_clock(tclock1)
    do i = 1, m
        do j = 1, n
           a(i,j) = 0.d0
           enddo
        enddo
    call system_clock(tclock2, clock_rate)
    elapsed_time = float(tclock2 - tclock1) / float(clock_rate)
    print 11, elapsed_time
 11 format("Elapsed time = ",f12.8, " seconds")
  

    call system_clock(tclock1)   
    do j = 1, n
        do i = 1, m
           a(i,j) = 0.d0
           enddo
        enddo    
    call system_clock(tclock2, clock_rate)
    elapsed_time = float(tclock2 - tclock1) / float(clock_rate)
    print 11, elapsed_time

end program looporder

