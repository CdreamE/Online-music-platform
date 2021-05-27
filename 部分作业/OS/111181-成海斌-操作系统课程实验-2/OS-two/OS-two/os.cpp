#include<iostream>
#include<string>
#include<stdio.h>
using namespace std;
#define False 0
#define True 1
int Max[100][100] = { 0 };//���������������Դ���������
int Avaliable[100] = { 0 };//ϵͳ������Դ
char name[100] = { 0 };//��Դ������
int Allocation[100][100] = { 0 };//ϵͳ�ѷ�����Դ
int Need[100][100] = { 0 };//����Ҫ��Դ
int Request[100] = { 0 };//������Դ����
int temp[100] = { 0 };//��Ű�ȫ����
int Work[100] = { 0 };//���ϵͳ���ṩ��Դ
int M = 100;//��ҵ�������Ϊ100
int N = 100;//��Դ�������Ϊ100
void showdata()//��ʾ��Դ����
{
    int i, j;
    cout << "ϵͳĿǰ���õ���Դ[Avaliable]:" << endl;
    for (i = 0; i < N; i++)
        cout << name[i] << " ";
    cout << endl;
    for (j = 0; j < N; j++)
        cout << Avaliable[j] << " ";//���������Դ
    cout << endl;
    cout << "             Max      Allocation     Need" << endl;
    cout << "������      ";
    for (j = 0; j < 3; j++) {
        for (i = 0; i < N; i++)
            cout << name[i] << " ";
        cout << "      ";
    }
    cout << endl;
    for (i = 0; i < M; i++) {
        cout << " " << i << "         ";
        for (j = 0; j < N; j++)
            cout << Max[i][j] << " ";
        cout << "      ";
        for (j = 0; j < N; j++)
            cout << Allocation[i][j] << " ";
        cout << "      ";
        for (j = 0; j < N; j++)
            cout << Need[i][j] << " ";
        cout << endl;
    }
}

int changdata(int i)//������Դ����
{
    int j;
    for (j = 0; j < M; j++) {
        Avaliable[j] = Avaliable[j] - Request[j];
        Allocation[i][j] = Allocation[i][j] + Request[j];
        Need[i][j] = Need[i][j] - Request[j];
    }
    return 1;
}
int safe()//��ȫ���㷨
{
    int i, k = 0, m, apply, Finish[100] = { 0 };
    int j;
    int flag = 0;
    Work[0] = Avaliable[0];
    Work[1] = Avaliable[1];
    Work[2] = Avaliable[2];
    for (i = 0; i < M; i++) {
        apply = 0;
        for (j = 0; j < N; j++) {
            if (Finish[i] == False && Need[i][j] <= Work[j]) {
                apply++;
                if (apply == N) {
                    for (m = 0; m < N; m++)
                        Work[m] = Work[m] + Allocation[i][m];//�������
                    Finish[i] = True;
                    temp[k] = i;
                    i = -1;
                    k++;
                    flag++;
                }
            }
        }
    }
    for (i = 0; i < M; i++) {
        if (Finish[i] == False) {
            cout << "ϵͳ����ȫ" << endl;//���ɹ�ϵͳ����ȫ
            return -1;
        }
    }
    cout << "ϵͳ�ǰ�ȫ��!" << endl;//�����ȫ������ɹ�
    cout << "��ȫ����:";
    for (i = 0; i < M; i++) {//������н�������
        cout << temp[i];
        if (i < M - 1) cout << "->";
    }
    cout << endl;
    return 0;
}
void request()//�������м��㷨��������Դ�Խ����ж�
{
    char ch;
    int i = 0, j = 0;
    ch = 'y';
    cout << "������Ҫ��������Դ���̺�(0-" << M - 1 << "):";
    cin >> i;//�������������Դ��
    cout << "��������� " << i << " �������Դ:" << endl;
    for (j = 0; j < N; j++)
    {
        cout << name[j] << ":";
        cin >> Request[j];//������Ҫ�������Դ
    }
    for (j = 0; j < N; j++) {
        if (Request[j] > Need[i][j])//�ж������Ƿ�������������������
        {
            cout << "���� " << i << "�������Դ��������Ҫ����Դ";
            cout << " ���䲻����������䣡" << endl;
            ch = 'n';
            break;
        }
        else {
            if (Request[j] > Avaliable[j])//�ж������Ƿ���ڵ�ǰ��Դ����������
            {                         //����
                cout << "����" << i << "�������Դ����ϵͳ���ڿ����õ���Դ";
                cout << " ��������������!" << endl;
                ch = 'n';
                break;
            }
        }
    }
    if (ch == 'y') {
        changdata(i);//���ݽ����������任��Դ
        showdata();//���ݽ�����������ʾ�任�����Դ
        safe();//���ݽ����������������м��㷨�ж�
    }
}

int main()//������
{

    int i, j, number, m, n, flag, choice=0;
    char ming;
    cout << "*****************�������ϵͳ���̵���ʵ��*****************" << endl;
    cout << "����������ϵͳ�ɹ���Դ���������:";
    cin >> n;
    N = n;
    for (i = 0; i < n; i++)
    {
        cout << "��Դ" << i + 1 << "������:";
        cin >> ming;
        name[i] = ming;
        cout << "��Դ������:";
        cin >> number;
        Avaliable[i] = number;
    }
    cout << endl;
    cout << "��������ҵ������:";
    cin >> m;
    M = m;
    cout << "����������̵����������(" << m << "*" << n << "����)[Max]:" << endl;
    for (i = 0; i < m; i++)
        for (j = 0; j < n; j++)
            cin >> Max[i][j];
    do {
        flag = 0;
        cout << "������������Ѿ��������Դ��(" << m << "*" << n << "����)[Allocation]:" << endl;
        for (i = 0; i < m; i++)
            for (j = 0; j < n; j++) {
                cin >> Allocation[i][j];
                if (Allocation[i][j] > Max[i][j])
                    flag = 1;
                Need[i][j] = Max[i][j] - Allocation[i][j];
            }
        if (flag)
            cout << "�������Դ�������������������������!\n";
    } while (flag);

    showdata();//��ʾ������Դ
    safe();//�����м��㷨�ж�ϵͳ�Ƿ�ȫ
    while (choice)
    {
        cout << "***********************************" << endl;
        cout << "             0:�뿪        " << endl;
        cout << "             1:��Դ����    " << endl;
        cout << "��ѡ���ܺţ�";
        cin >> choice;
        switch (choice)
        {
        case 1: request(); break;
        case 0: choice = 0; break;
        default: cout << "����ȷѡ���ܺ�(0-1)!" << endl; break;
        }
    }
    return 1;
}
