export interface Meta {
    key: string;
    props: Prop[];
}

export interface Prop {
    name: string;
    label: string;
    labels?: string[];
    values?: object;
    type?: string;
    valueKey?: string;
}

export interface Page {
    type: string;
    search: Prop[];
    ops: Operate[];
    toolbar: Operate[];
}

export interface Operate {
    type: string;
    label: string;
    service?: string;
    success?: string;
    parameter?: object;
}