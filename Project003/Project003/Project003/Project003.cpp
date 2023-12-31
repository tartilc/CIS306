#include "stdafx.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <fstream>

using namespace std;

ofstream output;

struct node
{
	int value;
	node* left;
	node* right;
};

struct node* root;

struct node* insert(struct node* r, int data);
void inOrder(struct node* r);
void preOrder(struct node* r);
void postOrder(struct node* r);

int main()
{
	ifstream input;
	input.open("input.txt");
	output.open("output.txt");

	root = NULL;
	int v;

	while (!input.eof())
	{
		input >> v;
		root = insert(root, v);
	}

	cout << "Inorder Traversal: ";
	output << "Inorder Traversal: ";
	inOrder(root);
	cout << endl;
	output << endl;

	cout << "Preorder Traversal: ";
	output << "Preorder Traversal: ";
	preOrder(root);
	cout << endl;
	output << endl;

	cout << "Postorder Traversal: ";
	output << "Postorder Traversal: ";
	postOrder(root);
	cout << endl;
	output << endl;

	input.close();
	output.close();

	//system("pause");
	return 0;
}

struct node* insert(struct node* r, int data)
{
	if (r == NULL)
	{
		r = (struct node*) malloc(sizeof(struct node));
		r->value = data;
		r->left = NULL;
		r->right = NULL;
	}
	else if (data < r->value) {
		r->left = insert(r->left, data);
	}
	else {
		r->right = insert(r->right, data);
	}
	return r;

}

void inOrder(struct node* r)
{
	if (r != NULL) {
		inOrder(r->left);
		cout << r->value << " ";
		output << r->value << " ";
		inOrder(r->right);
	}
}

void preOrder(struct node* r)
{
	if (r != NULL) {
		cout << r->value << " ";
		output << r->value << " ";
		preOrder(r->left);
		preOrder(r->right);
	}
}

void postOrder(struct node* r)
{
	if (r != NULL) {
		postOrder(r->left);
		postOrder(r->right);
		cout << r->value << " ";
		output << r->value << " ";
	}
}