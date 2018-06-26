import * as React from 'react';
import message from '../util/message';
import Icon from '../ui/icon';
import './i18n';
import './page.less';

export default class Page<T extends Object={}, E extends object={}> extends React.Component<T, E> {
    render(): JSX.Element {
        let title = message.get(this.getTitle());
        document.title = title;

        return (
            <div id="ranch-ui-mobile">
                <div className="layout-top">
                    <div className="title">{title}</div>
                </div>
                <div className="layout-content">{this.getContent()}</div>
                {this.bottom()}
            </div>
        );
    }

    protected getTitle(): string {
        return 'title';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return null;
    }

    private bottom(): JSX.Element {
        let items = [{
            icon: '\ue602',
            name: 'index'
        }, {
            icon: '\ue604',
            name: 'doc'
        },{
            icon: '\ue604',
            name: 'by'
        }, {
            icon: '\ue603',
            name: 'mine'
        }];

        return (
            <div className="layout-bottom">
                <table cellSpacing="0">
                    <tbody>
                        <tr>
                            {items.map((item, index) =>
                                <td key={index}>
                                    <a href={item.name + '.html'} className={this.getBottom() === index ? 'active' : 'inactive'}>
                                        <Icon code={item.icon} />
                                        <div className="label">{message.get(item.name)}</div>
                                    </a>
                                </td>
                            )}
                        </tr>
                    </tbody>
                </table>
            </div>
        );
    }

    protected getBottom(): number {
        return 0;
    }
}