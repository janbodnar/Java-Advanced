// there is a subtle difference between the 
// prefix and suffix increment operators

void main() {

    int num = 0;

    num = num++;

    IO.println(num);

    num = ++num;
    
    IO.println(num);
}
