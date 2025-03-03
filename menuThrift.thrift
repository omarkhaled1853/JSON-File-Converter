namespace java org.example

struct MenuItem {
    1: string id
    2: optional string label
}

struct ThriftMenu {
    1: string header
    2: list<MenuItem> items
}

struct MenuWrapper {
    1: ThriftMenu menu
}
