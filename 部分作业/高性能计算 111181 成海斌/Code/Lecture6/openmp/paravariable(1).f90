! Lecture6/openmp/paravariable.f90

program paravariable
	use omp_lib
	implicit none
	integer :: i

        real(kind=8), dimension(10) :: y
        real(kind=8) :: dx, x

        dx = 2

        !$omp parallel do private(x, dx)
        do i = 1,10
           x =  i * dx
           y(i) = x
        enddo

        do i = 1,10
           print *, y(i)
        enddo
end program paravariable
   