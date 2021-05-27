! Lecture6/openmp/temp.f90

program temp
        use omp_lib
        implicit none
	
        integer :: i,thread_num

	!$omp parallel private(thread_num)
        do i=1, 10
            !$ thread_num = omp_get_thread_num()
	    !$omp critical
            !$ print "('Thread ',i2, ' i=', i2)", thread_num, i
            !$omp end critical
        enddo
	
        !$omp end parallel

end program temp

        