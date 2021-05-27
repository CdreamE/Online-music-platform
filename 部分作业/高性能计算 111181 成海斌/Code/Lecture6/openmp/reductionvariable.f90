! Lecture6/openmp/reductionvariable.f90

program reductionvariable
	use omp_lib
	implicit none
	integer :: i, thread_num

        real(kind=8) :: sum

        sum = 0.d0

        !$omp parallel do reduction(+: sum)
        do i = 1, 15
           sum = sum + i
           !$omp critical
           !$ thread_num = omp_get_thread_num() 
           !$ print "('Thread ', i2, ' i = ', i2, ' sum = ', f6.2)", thread_num, i, sum
           !$omp end critical
        enddo
    
        print *, "sum = ", sum
end program reductionvariable
