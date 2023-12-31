#include "stdafx.h"
#include "iostream"
#include "fstream"
#include "stdlib.h"
#include "string.h"
#include "ctype.h"
#include "locale"

using namespace std;

int isKeyword(char buffer[]) {
	char keywords[6][10] = { "else","if","int","return","void","while" };
	int i, flag = 0;

	for (i = 0; i < 6; ++i) {
		if (strcmp(keywords[i], buffer) == 0) {
			flag = 1;
			break;
		}
	}

	return flag;
}

int main() {
	char ch, buffer[15], operators[] = "+-*/%=";
	char operdel[21][10] = { "+","-","*","/","<","<=",">",">=","==","!=","=",";",",","(",")","[","]","{","}","/*","*/" };
	ifstream input("input.txt");
	ofstream output;
	output.open("output.txt");
	

	int i, j = 0;

	if (!input.is_open()) {
		cout << "error";
		exit(0);
	}

	while (!input.eof()) {
		ch = input.get();

		
		for (i = 0; i < 6; ++i) {
			if (ch == operators[i]) {
				output << ch << " is operator\n";
				cout << ch << " is operator\n";
			}
		}
		

		if (isalnum(ch)) {
			buffer[j++] = ch;
		}
		else if ((ch == ' ' || ch == '\n') && (j != 0)) {
			buffer[j] = '\0';
			j = 0;

			if (isKeyword(buffer) == 1){
				output << buffer << " is keyword\n";
				cout << buffer << " is keyword\n";
			}
			else {
				output << buffer << " is identifier\n";
				cout << buffer << " is identifier\n";
			}
		}

	}

	input.close();
	output.close();

	system("pause");

	return 0;
}